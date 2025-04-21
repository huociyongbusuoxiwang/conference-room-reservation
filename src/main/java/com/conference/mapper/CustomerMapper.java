package com.conference.mapper;

import com.conference.entity.Customer;

public interface CustomerMapper {

    // 根据用户名查询客户
    Customer findByUsername(String username);

    // 添加客户 - 客户注册
    void addCustomer(Customer customer);
}
