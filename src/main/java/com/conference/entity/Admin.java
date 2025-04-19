package com.conference.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Admin {
    @NotNull    // 整形非空
    private Integer userId; // 管理员编号

    private String username;    // 登录用户名
    private String password;    // 密码
    private String name;    // 姓名

    @Email  // 邮箱格式校验
    @NotEmpty   // 字符串非空
    private String email;   // 邮箱

    private String phoneNumber; // 手机号
}
