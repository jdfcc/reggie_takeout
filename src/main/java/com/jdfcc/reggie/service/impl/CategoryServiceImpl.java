package com.jdfcc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdfcc.reggie.common.CustomException;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Category;
import com.jdfcc.reggie.entity.Dish;
import com.jdfcc.reggie.entity.Setmeal;
import com.jdfcc.reggie.mapper.CategoryMapper;
import com.jdfcc.reggie.service.CategoryService;
import com.jdfcc.reggie.service.DishService;
import com.jdfcc.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper mapper;

    @Autowired
    private CategoryService CategoryService;

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private DishService dishService;

    public R<String> saveCategory(Category category) {
        mapper.insert(category);
        return R.success("Save item ok");
    }

    @Override
    public R<Page> selectPage(int page, int pageSize) {
        Page<Category> pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        Page<Category> p = mapper.selectPage(pageInfo, wrapper);
        return R.success(p);
    }


    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if(count1 > 0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setMealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0){
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除分类
        super.removeById(id);
    }
}
