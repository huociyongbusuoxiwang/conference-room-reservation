package com.conference.mapper;

import com.conference.entity.Customer;

public interface CustomerMapper {

    // 根据用户名查询客户
    Customer findByUsername(String username);

    // 根据id查询客户信息
    Customer findByCustomerId(Integer customerId);

    // 添加客户 - 客户注册
    void addCustomer(Customer customer);

    // 修改客户信息
    void updateCustomer(Customer customer);

    // 根据id删除客户
    void deleteCustomer(Integer customerId);
}
