package com.jdfcc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdfcc.reggie.entity.Setmeal;
import com.jdfcc.reggie.mapper.SetmealMapper;
import com.jdfcc.reggie.service.SetMealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetMealService {
}
