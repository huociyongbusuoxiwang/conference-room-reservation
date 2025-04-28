package com.conference.service.impl;

import com.conference.entity.Customer;
import com.conference.mapper.CustomerMapper;
import com.conference.service.CustomerService;
import com.conference.utils.MD5Util;
import com.conference.utils.Result;
import com.conference.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    // 查询客户列表
    @Override
    public Result list() {
        List<Customer> customers = customerMapper.list();
        return Result.success(customers);
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
            customer.setPassword(MD5Util.encrypt(customer.getPassword()+MD5Util.KEY)); // 注册需要加密密码
            customerMapper.addCustomer(customer);   // 注：注册后状态默认为审核中
            return Result.success("账号注册成功，管理员审核中");
        }else{
            // 用户名已存在，则返回用户已存在的信息
            return Result.error("用户名已存在");
        }
    }

    // 根据id查询客户信息
    @Override
    public Customer findByCustomerId(Integer customerId) {
        return customerMapper.findByCustomerId(customerId);
    }

    // 查询各个状态的客户信息
    @Override
    public Customer findByStatus(String status) {
        return customerMapper.findByStatus(status);
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

    // 更新密码 - 需二次确认
    @Override
    public void updatePwd(String newPwd, Integer customerId) {
        // 需要将加密后的密码传入
        customerMapper.updatePwd(MD5Util.encrypt(newPwd+MD5Util.KEY), customerId);
    }
}
