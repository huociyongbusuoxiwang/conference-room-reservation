package com.conference.mapper;

import com.conference.entity.Employee;
import com.conference.utils.Result;

import java.util.List;

public interface EmployeeMapper {

    // 查询员工列表
    List<Employee> list();

    // 根据用户名查询员工信息
    Employee findByUsername(String username);

    // 根据编号查询员工
    Employee findByEmployeeId(Integer employeeId);

    // 查询状态为status的员工
    List<Employee> findByStatus(String status);

    // 添加员工
    void addEmployee(Employee employee);

    // 修改员工信息
    void updateEmployee(Employee employee);

    // 根据id删除员工
    void deleteEmployee(Integer employeeId);

    // 更新密码
    void updatePwd(String newPwd, Integer employeeId);

}
