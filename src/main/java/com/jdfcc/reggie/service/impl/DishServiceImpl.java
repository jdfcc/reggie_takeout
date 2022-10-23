package com.jdfcc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdfcc.reggie.entity.Dish;
import com.jdfcc.reggie.mapper.DishMapper;
import com.jdfcc.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
