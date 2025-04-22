package com.conference.service;

import com.conference.entity.Employee;
import com.conference.utils.Result;
import jakarta.validation.constraints.Pattern;

public interface EmployeeService {

    // 根据用户名查询员工
    Employee findByUsername(String username);

    // 根据员工编号查询员工信息
    Employee findByEmployeeId(Integer employeeId);

    // 员工注册
    Result regist(Employee employee);

    // 修改员工信息
    void updateEmployee(Employee employee);

    // 根据id删除员工
    void deleteEmployee(Integer employeeId);

}
