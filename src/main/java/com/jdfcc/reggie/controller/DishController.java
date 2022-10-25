package com.jdfcc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Dish;
import com.jdfcc.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService service;

    @GetMapping("/page")
    public R<Page> selectPage(int page, int pageSize, String name) {
        R r = service.selectPage(page, pageSize, name);
        return r;
    }

    @PostMapping("/status/0")
    public R<String> setStatusOff(Long ids) {
        String id = String.valueOf(ids);
        if (id.contains(",")) {
//            List<Long> list = new ArrayList<>();
            log.info("many");
        }
//        log.info(String.valueOf(ids));
            R r = service.stopSeal(ids);
        return r;
    }

    @PostMapping("/status/1")
    public R<String> setStatusOn(String ids) {
        log.info(String.valueOf(ids));
        R r = service.beginSeal(Long.valueOf(ids));
        return r;
    }

}
