package com.jdfcc.reggie;

import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Dish;
import com.jdfcc.reggie.entity.Employee;
import com.jdfcc.reggie.mapper.DishMapper;
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
public class test {

    @Autowired
    private EmployeeService service;

    @Autowired
    private DishMapper mapper;

    @Test
    public void TestSplit(){
        String s="123456,6666666";
        String as="123456";
        String[] a = as.split(",");
        for(String val:a){
            System.out.println(val);
        }
        System.out.println(a.length);
    }

    @Test
    public void TestSearch(){
       Dish dish=mapper.selectById("1397849739276890114");
        System.out.println(dish.toString());
    }
}
