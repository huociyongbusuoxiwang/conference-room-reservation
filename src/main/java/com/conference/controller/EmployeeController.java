package com.conference.controller;

import com.conference.entity.Employee;
import com.conference.service.EmployeeService;
import com.conference.utils.JwtUtil;
import com.conference.utils.MD5Util;
import com.conference.utils.Result;
import com.conference.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**  登录功能实现
     * 登录需求
     * 地址: /employee/login
     * 方式: post
     * 参数:
     *    {
     *     "username":"zhangsan", //用户名
     *     "password":"123456"     //明文密码
     *    }
     * 返回:
     *   {
     *    "code":"0",         // 成功状态码
     *    "message":"success"   // 成功状态描述
     *    "data":{
     *         "token":"... ..." // 用户id的token
     *     }
     *  }
     *
     * 大概流程:
     *    1. 账号进行数据库查询 返回用户对象
     *    2. 对比用户密码(md5加密)
     *    3. 成功,根据adminId生成的token -> map key=token value=token值 - result封装
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
        Employee loginUser = employeeService.findByUsername(username);
        // 判断用户是否存在
        if (loginUser == null) return Result.error("用户不存在");
        // 存在则校验用户密码
        if(MD5Util.encrypt(password+MD5Util.KEY).equals(loginUser.getPassword())){
            // 登录成功，获取token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getEmployeeId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);

            // 返回token
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    /**员工注册 - 添加员工
     * url地址：employee/regist
     * 请求方式：POST
     * 请求参数：
     * {
     *     "username":"员工1",
     *     "password":"123456",
     *     "name":"员工姓名1"
     *     "email":"1@qq.com"
     *     "phoneNumber":"12345678901"
     * }
     * 响应数据：
     * {
     *    "code":"0",
     *    "message":"success"
     *    "data":{}
     * }
     *
     * 实现步骤:
     *   1. 将密码加密
     *   2. 将数据插入
     *   3. 判断结果,成 返回0 失败 1
     */
    @PostMapping("regist")
    public Result regist(@RequestBody Employee employee){
        return employeeService.regist(employee);
    }

    /** 查询员工列表
     *  url地址：employee
     *  请求方式：GET
     *  响应数据：
     *  {
     *      "code":"0",
     *      "message":"success",
     *      "data":[
     *          employee1:{},
     *          employee2:{},
     *          ...
     *      ]
     *  }
     */
    @GetMapping
    public Result list(){
        Result result = employeeService.list();
        return result;
    }

    /** 根据编号查询员工信息
     *  url地址：employee/employeeDetail
     *  请求方式：GET
     *  请求参数：
     *  {
     *      "employeeId":员工编号
     *  }
     *  响应数据：
     *  {
     *      "code":"0",
     *      "message":"success",
     *      "data":{
     *          "username":"员工用户名",
     *          "name":"员工姓名",
     *          "email":"员工邮箱",
     *          "phoneNumber":"员工电话号码"
     *      }
     *  }
     */
    @GetMapping("employeeDetail")
    public Result employeeDetail(Integer employeeId){
        Employee employee = employeeService.findByEmployeeId(employeeId);
        return Result.success(employee);
    }

     /** 修改员工基本信息
      *  url地址：employee
      *  请求方式：PUT
      *  请求参数：
      *  {
      *      "employeeId":员工编号,
      *      "username":"员工用户名",
      *      "name":"员工姓名",
      *      "email":"员工邮箱",
      *      "phoneNumber":"员工电话号码"
      *  }
      *  响应数据：
      *  {
      *      "code":"0",
      *      "message":"success",
      *      "data":{}
      *  }
      */
    @PutMapping
    public Result updateEmployee(@RequestBody Employee employee){
        employeeService.updateEmployee(employee);
        return Result.success();
    }

    /** 根据id删除员工
     *  url地址：employee
     *  请求方式：DELETE
     *  请求参数：
     *  {
     *      "employeeId":员工编号
     *  }
     *  响应数据：
     *  {
     *      "code":"0",
     *      "message":"success",
     *      "data":{}
     *  }
     */
    @DeleteMapping
    public Result deleteEmployee(Integer employeeId){
        employeeService.deleteEmployee(employeeId);
        return Result.success();
    }

    /** 更新密码 - 需二次确认
     *  url地址：admin
     *  请求方式：PATCH
     *  请求参数：
     *  {
     *      "old_pwd":"原密码",
     *      "new_pwd":"新密码",
     *      "re_pwd":"确认密码"
     *  }
     *  响应数据：
     *  {
     *      "code":"0",
     *      "message":"success"
     *      "data":{}
     *  }
     */
    @PatchMapping
    public Result updatePassword(@RequestBody Map<String, String> params){
        // 1.校验参数
        String oldPwd = params.get("old_pwd");  // 旧密码
        String newPwd = params.get("new_pwd");  // 新密码
        String rePwd = params.get("re_pwd");    // 确认密码

        // (1)判断是否为空
        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的参数");
        }

        // (2)判断原密码是否正确
        // 根据用户名获取原密码，再和输入的old_pwd比对
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        Employee loginUser = employeeService.findByUsername(username);
        // 获取的密码是加密后的，需要将旧密码先加密再比较
        if(!loginUser.getPassword().equals(MD5Util.encrypt(oldPwd+MD5Util.KEY))) return Result.error("原密码错误，请重新输入");

        // (3)判断new_pwd和old_pwd是否相同
        if(newPwd.equals(oldPwd)) return Result.error("新密码不能与原密码相同，请重新输入");

        // (4)判断new_pwd和re_pwd是否一致
        if(!rePwd.equals(newPwd)){
            return Result.error("新密码和确认密码不一致，请重新输入");
        }

        // 2.密码更新
        employeeService.updatePwd(newPwd);
        return Result.success();
    }
}
