package com.jdfcc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.dto.DishDto;
import com.jdfcc.reggie.entity.Dish;

public interface DishService extends IService<Dish> {


    public R<String> setStatus(String id,String status);

    public void saveWithFlavor(DishDto dish);

    public R<Page> selectPageWithFlavor(int page, int size, String name);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void deleteWithFlavor(String ids);


}
