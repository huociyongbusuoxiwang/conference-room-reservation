package com.conference.service;

import com.conference.entity.Admin;
import com.conference.utils.Result;
import jakarta.validation.constraints.Pattern;

public interface AdminService {

    // 注册管理员
    Result regist(Admin admin);
}
