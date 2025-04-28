package com.conference.service.impl;

import com.conference.entity.Employee;
import com.conference.mapper.EmployeeMapper;
import com.conference.service.EmployeeService;
import com.conference.utils.MD5Util;
import com.conference.utils.Result;
import com.conference.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    // 查询员工列表
    @Override
    public Result list() {
        List<Employee> employeeList = employeeMapper.list();
        return Result.success(employeeList);
    }

    // 根据用户名查询员工
    @Override
    public Employee findByUsername(String username) {
        return employeeMapper.findByUsername(username);
    }

    // 根据编号查询员工信息
    @Override
    public Employee findByEmployeeId(Integer employeeId) {
        return employeeMapper.findByEmployeeId(employeeId);
    }

    // 员工注册 - 添加员工
    @Override
    public Result regist(Employee employee) {
        // 查询用户名是否已存在
        Employee newEmployee = employeeMapper.findByUsername(employee.getUsername());
        if (newEmployee == null){
            // 用户名不存在，则注册
            employee.setPassword(MD5Util.encrypt(employee.getPassword()+MD5Util.KEY)); // 注册需要加密密码
            employeeMapper.addEmployee(employee); // 注：注册后状态默认为审核中
            return Result.success("账号注册成功，管理员审核中");
        }else {
            // 用户名已存在，则返回用户已存在的信息
            return Result.error("用户名已存在");
        }
    }

    // 查询状态为status的员工
    @Override
    public Result findByStatus(String status) {
        List<Employee> employeeList = employeeMapper.findByStatus(status);
        return Result.success(employeeList);
    }

    // 修改员工信息
    @Override
    public void updateEmployee(Employee employee) {
        employeeMapper.updateEmployee(employee);
    }

    // 根据id删除员工
    @Override
    public void deleteEmployee(Integer employeeId) {
        employeeMapper.deleteEmployee(employeeId);
    }

    // 更新密码
    @Override
    public void updatePwd(String newPwd, Integer employeeId) {
        // 需要将加密后的密码传入
        employeeMapper.updatePwd(MD5Util.encrypt(newPwd+MD5Util.KEY), employeeId);
    }
}
