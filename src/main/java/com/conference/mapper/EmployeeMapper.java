package com.conference.mapper;

import com.conference.entity.Employee;
import com.conference.utils.Result;

public interface EmployeeMapper {

    // 根据用户名查询员工信息
    Employee findByUsername(String username);

    // 根据编号查询员工
    Employee findByEmployeeId(Integer employeeId);

    // 添加员工
    void addEmployee(Employee employee);

    // 修改员工信息
    void updateEmployee(Employee employee);

    // 删除员工
    void deleteEmployee(Integer employeeId);

}
