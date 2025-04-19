package com.conference.service;

import com.conference.entity.Admin;
import com.conference.utils.Result;
import jakarta.validation.constraints.Pattern;

public interface AdminService {

    // 根据用户名查询管理员
    Admin findByUserName(String username);

    // 注册管理员
    Result regist(Admin admin);
}
