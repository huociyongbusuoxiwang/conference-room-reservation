package com.conference.mapper;

import com.conference.entity.Employee;

public interface EmployeeMapper {

    // 根据用户名查询员工
    Employee findByUsername(String username);

    // 添加员工
    void addEmployee(Employee employee);

}
