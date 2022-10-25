package com.jdfcc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    public R<Page> selectPage(int page,int size,String name);

    public R<String> stopSeal(Long id);

    public R<String> beginSeal(Long id);
}
