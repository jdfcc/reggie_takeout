package com.jdfcc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Employee;
import com.jdfcc.reggie.mapper.EmployeeMapper;
import com.jdfcc.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper mapper;
    @Autowired
    private EmployeeService service;

    @Override
    public R login(HttpServletRequest request, Employee employee) {
        String pw = employee.getPassword();
        pw = DigestUtils.md5DigestAsHex(pw.getBytes());
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = service.getOne(wrapper);
        if (emp == null)
            return R.error("login failed");
        if (!pw.equals(emp.getPassword()))
            return R.error("wrong password");
        if (emp.getStatus() == 0)
            return R.error("Account disabled");
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @Override
    public R logout(HttpServletRequest request) {

//清楚浏览器缓存里面的session
        request.getSession().removeAttribute("employee");
        return R.success("success quit");
    }

    public R saveEmployee(Employee employee) {
        service.save(employee);
        return R.success("Succeed to create employee");
    }

    @Override
    public R alterStatus(HttpServletRequest request, Employee employee) {
//        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Employee::getId, employee.getStatus());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        mapper.updateById(employee);
        return R.success("Successfully update");
    }

    @Override
    public R<Page> selectPage(int page, int size, String name) {

        Page pageInfo = new Page(page, size);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
        wrapper.orderByDesc(Employee::getUpdateTime);
        Page val = mapper.selectPage(pageInfo, wrapper);
        return R.success(val);
//        service.page(pageInfo,wrapper);
//        return R.success(pageInfo);
    }
}
