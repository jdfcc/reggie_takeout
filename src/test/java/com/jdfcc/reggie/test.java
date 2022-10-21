package com.jdfcc.reggie;

import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Employee;
import com.jdfcc.reggie.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootTest(classes = ReggieApplication.class)
@RestController
@RequestMapping("/test")
public class test {

    @Autowired
    private EmployeeService service;

    @Test
    @PostMapping("/login")
    public R TestLogin(HttpServletRequest request,@RequestBody Employee employee){
        return service.login(request, employee);
    }
}
