package com.conference.service;

import com.conference.entity.Customer;
import com.conference.utils.Result;

import java.util.List;

public interface CustomerService {

    // 查询客户列表
    Result list();

    // 根据用户名查询用户
    Customer findByUsername(String username);

    // 根据id查询客户信息
    Customer findByCustomerId(Integer customerId);

    // 用户注册
    Result regist(Customer customer);

    // 修改客户信息
    void updateCustomer(Customer customer);

    // 根据id删除客户
    void deleteCustomer(Integer customerId);

    // 更新密码 - 需二次确认
    void updatePwd(String newPwd, Integer customerId);
}
