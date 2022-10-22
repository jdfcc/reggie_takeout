package com.jdfcc.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Employee;
import com.jdfcc.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    /**
     * 登录
     */
    @PostMapping("/login")
    public R login(HttpServletRequest request, @RequestBody Employee employee) {
        return service.login(request, employee);
    }

    /**
     * 退出
     */
    @PostMapping("/logout")
    public R logout(HttpServletRequest request) {
        return service.logout(request);
    }

    /**
     * 新增员工
     */
    @PostMapping
    public R save(HttpServletRequest request, @RequestBody Employee employee) {
        //初始化密码,对密码进行md5加密
        log.info("Creat employee: {}", employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empID = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empID);
        employee.setUpdateUser(empID);
        return service.saveEmployee(employee);
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> selectPage(int page, int pageSize, String name) {
        log.info("page = {}, size= {}, name= {}",page,pageSize,name);
        return service.selectPage(page, pageSize, name);
    }

    /**
     * 修改员工账号状态
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R alterEmployee(HttpServletRequest request,@RequestBody Employee employee){
        log.info("Update: {}",employee.toString());
        R r=service.alterStatus(request,employee);
        return r;
    }

}
