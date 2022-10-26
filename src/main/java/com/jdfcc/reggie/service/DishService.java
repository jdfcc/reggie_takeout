package com.jdfcc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    public R<Page> selectPage(int page,int size,String name);

    public R<String> stopSeal(String id);

    public R<String> beginSeal(String id);

    public R<String> add(Dish dish);

    public Dish search(Long id);
}
