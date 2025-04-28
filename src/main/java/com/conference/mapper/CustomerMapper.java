package com.conference.mapper;

import com.conference.entity.Customer;

import java.util.List;

public interface CustomerMapper {

    // 查询客户列表
    List<Customer> list();

    // 根据用户名查询客户
    Customer findByUsername(String username);

    // 根据id查询客户信息
    Customer findByCustomerId(Integer customerId);

    // 查询各个状态的客户信息
    Customer findByStatus(String status);

    // 添加客户 - 客户注册
    void addCustomer(Customer customer);

    // 修改客户信息
    void updateCustomer(Customer customer);

    // 根据id删除客户
    void deleteCustomer(Integer customerId);

    // 更新密码 - 需二次确认
    void updatePwd(String newPwd, Integer customerId);

}
