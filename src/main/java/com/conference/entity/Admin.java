package com.conference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Admin {

    @NotNull    // 整形非空
    private Integer userId; // 管理员编号
    private String username;    // 登录用户名

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // springmvc将对象转换成json字符串时，忽略该字段，最后的json字符串中该属性就不会显示出来
    private String password;    // 密码
    private String name;    // 姓名

    @Email  // 邮箱格式校验
    @NotEmpty   // 字符串非空
    private String email;   // 邮箱

    @NotEmpty   // 字符串非空
    private String phoneNumber; // 手机号
}
