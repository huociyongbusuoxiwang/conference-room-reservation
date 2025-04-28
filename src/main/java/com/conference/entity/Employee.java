package com.conference.entity;

import com.conference.utils.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Employee {

    @NotNull    // 整形非空
    private Integer employeeId; // 员工编号
    private String username;    // 登录用户名

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // springmvc将对象转换成json字符串时，忽略该字段，最后的json字符串中该属性就不会显示出来
    private String password;    // 登录密码
    private String name;    // 员工姓名
    private String status = Status.CHECKING;  // 账号当前状态(注册提交后为审核中，其他状态为：已冻结、正常使用、审核不通过)

    @NotEmpty   // 字符串非空
    @Email  // 邮箱格式校验
    private String email;   // 员工邮箱

    @NotEmpty // 字符串非空
    private String phoneNumber; // 员工手机号
}
