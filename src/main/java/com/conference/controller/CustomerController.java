package com.conference.controller;

import com.conference.entity.Customer;
import com.conference.service.CustomerService;
import com.conference.utils.JwtUtil;
import com.conference.utils.MD5Util;
import com.conference.utils.Result;
import com.conference.utils.ResultCodeEnum;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**  登录功能实现
     * 登录需求
     * 地址: /customer/login
     * 方式: post
     * 参数:
     *    {
     *     "username":"客户1", //用户名
     *     "password":"123456"     //明文密码
     *    }
     * 返回:
     *   {
     *    "code":"200",         // 成功状态码
     *    "message":"success"   // 成功状态描述
     *    "data":{
     *         "token":"... ..." // 用户id的token
     *     }
     *  }
     *
     * 大概流程:
     *    1. 账号进行数据库查询 返回用户对象
     *    2. 对比用户密码(md5加密)
     *    3. 成功,根据customerId生成的token -> map key=token value=token值 - result封装
     *    4. 失败,判断账号还是密码错误,封装对应的枚举错误即可
     *
     * 正则表达式：^\S{5,16}$
     *      ^：表示匹配字符串的开头
     *      $：表示匹配字符串的的结尾
     *      \S：表示匹配非空白字符(即空格、制表符、换行符等)，且在引号内需要将\转义
     *      {5,16}：表示前面的字符（这里是\S）出现的次数范围是5到16次
     */
    @PostMapping("login")
    public Result login(@Pattern(regexp = "^\\S{2,16}$") String username,
                        @Pattern(regexp = "^\\S{5,16}$") String password){
        // 根据用户名查询用户
        Customer loginUser = customerService.findByUsername(username);
        // 判断用户是否存在
        if (loginUser == null) return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        // 存在则校验用户密码
        if(MD5Util.encrypt(password).equals(loginUser.getPassword())){
            // 登录成功，获取token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getCustomerId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);

            // 返回token
            return Result.ok(token);
        }
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    /**用户注册
     * url地址：customer/regist
     * 请求方式：POST
     * 请求参数：
     * {
     *     "username":"客户1",
     *     "password":"123456",
     *     "name":"客户姓名1"
     *     "email":"1@qq.com"
     *     "phoneNumber":"12345678901"
     * }
     * 响应数据：
     * {
     *    "code":"200",
     *    "message":"success"
     *    "data":{}
     * }
     *
     * 实现步骤:
     *   1. 将密码加密
     *   2. 将数据插入
     *   3. 判断结果,成 返回200 失败 505
     */
    @PostMapping("regist")
    public Result regist(@RequestBody Customer customer){
        return customerService.regist(customer);
    }
}
