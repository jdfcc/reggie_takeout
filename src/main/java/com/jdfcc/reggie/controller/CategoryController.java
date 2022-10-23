package com.jdfcc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Category;
import com.jdfcc.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService service;

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category:{}", category);
        R r = service.saveCategory(category);
        return r;
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> selectPage(int page, int pageSize) {
        log.info("Page: {},pageSize: {}", page, pageSize);
        R r = service.selectPage(page, pageSize);
        return r;
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("Id: {}", ids);
        service.remove(ids);
        return R.success("Deleted Success");
    }
}
