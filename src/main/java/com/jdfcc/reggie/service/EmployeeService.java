package com.jdfcc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    public R login(HttpServletRequest request,Employee employee);

    public R logout(HttpServletRequest request);

    public R saveEmployee(Employee employee);

    public R<Page> selectPage(int page,int size,String name);

    public R alterStatus(HttpServletRequest request,Employee employee);

    public R<Employee> getByID(Long id);
}
