package com.conference.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Customer {

    @NotNull    // 整形非空
    private Integer customerId; // 客户编号
    private String username;    // 登录用户名
    private String password;    // 密码
    private String name;    // 姓名
    private String companyName; // 公司名

    @NotEmpty // 字符串非空
    @Email  // 邮箱格式校验
    private String email;   // 邮箱

    @NotEmpty // 字符串非空
    private String phoneNumber; // 手机号

    // 不设置为非空，仅在退款申请时设置
    private Integer isVerified = 0; // 是否通过管理员审核，默认值为0

}
