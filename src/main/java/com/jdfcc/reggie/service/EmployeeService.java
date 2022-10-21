package com.jdfcc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    public R login(HttpServletRequest request,Employee employee);

    public R logout(HttpServletRequest request);

}
