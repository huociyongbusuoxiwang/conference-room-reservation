package com.conference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Employee {

    @NotNull    // 整形非空
    private Integer employeeId; // 员工编号
    private String username;    // 登录用户名

    @JsonIgnore // springmvc将对象转换成json字符串时，忽略该字段，最后的json字符串中该属性就不会显示出来
    private String password;    // 登录密码
    private String name;    // 员工姓名

    @NotEmpty   // 字符串非空
    @Email  // 邮箱格式校验
    private String email;   // 员工邮箱

    @NotEmpty // 字符串非空
    private String phoneNumber; // 员工手机号
}
