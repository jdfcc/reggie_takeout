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

import java.util.List;

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

    /**
     * 根据id修改分类消息
     *
     * @param category
     * @return
     */

    @PutMapping
    public R update(@RequestBody Category category) {
        log.info("Category info: {}", category);
        service.updateById(category);
        return R.success("Success update");
    }

    @GetMapping("/list")
//    public R<Category> selectList(int type) {
//        R r = service.selectList(type);
//        return r;
//    }
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = service.list(queryWrapper);
        return R.success(list);
    }

}
