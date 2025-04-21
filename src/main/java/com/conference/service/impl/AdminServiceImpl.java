package com.conference.service.impl;

import com.conference.entity.Admin;
import com.conference.mapper.AdminMapper;
import com.conference.service.AdminService;
import com.conference.utils.MD5Util;
import com.conference.utils.Result;
import com.conference.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;


    @Override
    public Admin findByUsername(String username) {
        return adminMapper.findByUsername(username);
    }

    @Override
    public Result regist(Admin admin) {
        Admin admin1 = adminMapper.findByUsername(admin.getUsername()); // 查询用户名是否已存在
        if (admin1 == null){
            // 用户名不存在，则注册
            admin.setPassword(MD5Util.encrypt(admin.getPassword())); // 注册需要加密密码
            adminMapper.addAdmin(admin);
            return Result.ok(null);
        }else {
            // 用户名已存在，则返回用户已存在的信息
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }
    }


}
