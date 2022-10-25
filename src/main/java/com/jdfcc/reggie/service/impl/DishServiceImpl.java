package com.jdfcc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Dish;
import com.jdfcc.reggie.mapper.DishMapper;
import com.jdfcc.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper mapper;

    @Override
    public R<Page> selectPage(int page, int size, String name) {
        Page page1 = new Page(page, size);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            wrapper.like(Dish::getName, name);
        }
        Page val = mapper.selectPage(page1, wrapper);

        return R.success(val);
    }

    @Override
    public R<String> stopSeal(Long id) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Dish::getId, id);
        Dish dish = mapper.selectOne(wrapper);
        dish.setStatus(0);
        mapper.update(dish, wrapper);
        return R.success("Successfully set status=0");
    }

    @Override
    public R<String> beginSeal(Long id) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Dish::getId, id);
        Dish dish = mapper.selectOne(wrapper);
        dish.setStatus(1);
        mapper.update(dish, wrapper);
        return R.success("Successfully set status=1");
    }
}
