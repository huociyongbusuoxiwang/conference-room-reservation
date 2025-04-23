package com.conference.service.impl;

import com.conference.entity.Customer;
import com.conference.mapper.CustomerMapper;
import com.conference.service.CustomerService;
import com.conference.utils.MD5Util;
import com.conference.utils.Result;
import com.conference.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    // 查询客户列表
    @Override
    public Result list() {
        List<Customer> customers = customerMapper.list();
        return Result.ok(customers);
    }

    // 根据用户名查询用户
    @Override
    public Customer findByUsername(String username) {
        return customerMapper.findByUsername(username);
    }

    // 用户注册
    @Override
    public Result regist(Customer customer) {
        Customer customer1 = customerMapper.findByUsername(customer.getUsername());
        if(customer1 == null){
            // 用户名不存在，则注册
            customer.setPassword(MD5Util.encrypt(customer.getPassword())); // 注册需要加密密码
            customerMapper.addCustomer(customer);
            return Result.ok(null);
        }else{
            // 用户名已存在，则返回用户已存在的信息
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }
    }

    // 根据id查询客户信息
    @Override
    public Customer findByCustomerId(Integer customerId) {
        return customerMapper.findByCustomerId(customerId);
    }


    // 修改客户信息
    @Override
    public void updateCustomer(Customer customer) {
        customerMapper.updateCustomer(customer);
    }

    // 根据id删除客户
    @Override
    public void deleteCustomer(Integer customerId) {
        customerMapper.deleteCustomer(customerId);
    }
}
