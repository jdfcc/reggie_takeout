package com.jdfcc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdfcc.reggie.dto.SetmealDto;
import com.jdfcc.reggie.entity.Setmeal;

public interface SetMealService extends IService<Setmeal> {

    public Page<SetmealDto> selectWithCategory(int page, int pageSize, String name);

    public void saveWithCategory(SetmealDto dto);

    public void update(SetmealDto dto);

    public void delete(String id);

    public void setStatus(int status,String id);

}
