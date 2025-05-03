package com.conference.service;

import com.conference.entity.Admin;
import com.conference.utils.Result;

public interface AdminService {

    // 根据用户名查询管理员
    Admin findByUsername(String username);

    // 注册管理员
    Result regist(Admin admin);

    // 更新密码
    void updatePwd(String newPwd, Integer userId);
}
