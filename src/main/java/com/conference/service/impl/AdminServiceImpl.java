package com.conference.service.impl;

import com.conference.entity.Admin;
import com.conference.mapper.AdminMapper;
import com.conference.service.AdminService;
import com.conference.utils.MD5Util;
import com.conference.utils.Result;
import com.conference.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;


    // 根据用户名查询管理员
    @Override
    public Admin findByUsername(String username) {
        Admin admin = adminMapper.findByUsername(username);
        return admin;
    }

    // 管理员注册 - 由于是直接内定，所以可以不需要
    @Override
    public Result regist(Admin admin) {
        Admin admin1 = adminMapper.findByUsername(admin.getUsername()); // 查询用户名是否已存在
        if (admin1 == null){
            // 用户名不存在，则注册
            admin.setPassword(MD5Util.encrypt(admin.getPassword()+MD5Util.KEY)); // 注册需要加密密码
            adminMapper.addAdmin(admin);
            return Result.success();
        }else {
            // 用户名已存在，则返回用户已存在的信息
            return Result.error("用户名已存在");
        }
    }

    // 更新密码
    @Override
    public void updatePwd(String newPwd, Integer userId) {
        // 需要将加密后的密码传入
        adminMapper.updatePwd(MD5Util.encrypt(newPwd+MD5Util.KEY), userId);
    }
}
