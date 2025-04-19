package com.conference.mapper;

import com.conference.entity.Admin;

public interface AdminMapper {

    // 根据用户名查询管理员信息
    Admin findByUsername(String username);

    // 添加管理员
    void addAdmin(Admin admin);
}
