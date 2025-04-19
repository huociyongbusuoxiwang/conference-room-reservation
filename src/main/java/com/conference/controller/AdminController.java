package com.conference.controller;

import com.conference.entity.Admin;
import com.conference.service.AdminService;
import com.conference.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    /**用户注册
     * url地址：admin/regist
     * 请求方式：POST
     * 请求参数：
     * {
     *     "username":"zhangsan",
     *     "password":"123456",
     *     "name":"张三"
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
    public Result regist(@RequestBody Admin admin){
        return adminService.regist(admin);
    }
}
