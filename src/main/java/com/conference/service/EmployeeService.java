package com.conference.service;

import com.conference.entity.Employee;
import com.conference.utils.Result;
import jakarta.validation.constraints.Pattern;

public interface EmployeeService {

    // 根据用户名查询员工
    Employee findByUsername(String username);

    // 员工注册
    Result regist(Employee employee);
}
