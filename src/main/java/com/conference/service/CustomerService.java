package com.conference.service;

import com.conference.entity.Customer;
import com.conference.utils.Result;

public interface CustomerService {

    // 根据用户名查询用户
    Customer findByUsername(String username);

    // 用户注册
    Result regist(Customer customer);

}
