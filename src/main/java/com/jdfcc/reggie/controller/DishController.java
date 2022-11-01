package com.jdfcc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.dto.DishDto;
import com.jdfcc.reggie.dto.SetmealDto;
import com.jdfcc.reggie.entity.Dish;
import com.jdfcc.reggie.entity.DishFlavor;
import com.jdfcc.reggie.entity.Setmeal;
import com.jdfcc.reggie.entity.SetmealDish;
import com.jdfcc.reggie.service.DishFlavorService;
import com.jdfcc.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService service;

    @Autowired
    private DishFlavorService flavorService;

    @GetMapping("/page")
    public R<Page> selectPage(int page, int pageSize, String name) {
        R r = service.selectPageWithFlavor(page, pageSize, name);
        return r;
    }

    @PostMapping("/status/0")
    public R<String> setStatusOff(String ids) {
        String id = String.valueOf(ids);
        if (id.contains(",")) {
//            List<Long> list = new ArrayList<>();
            log.info("many");
        }
//        log.info(String.valueOf(ids));
        R r = service.stopSeal(ids);
        return r;
    }

    @PostMapping("/status/1")
    public R<String> setStatusOn(String ids) {
        log.info(String.valueOf(ids));
        R r = service.beginSeal(ids);
        return r;
    }

    /**
     * 新增菜品
     *
     * @param dish
     * @return
     */
    @PostMapping
    public R<String> add(@RequestBody DishDto dish) {
        log.info("Dish: {}", dish.toString());
        service.saveWithFlavor(dish);
        return R.success("Successfully add");
    }

    @GetMapping("/{ids}")
    public R<DishDto> get(@PathVariable Long ids) {
        log.info("id: {}", ids);
        DishDto dish = service.getByIdWithFlavor(ids);
        return R.success(dish);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R update(@RequestBody DishDto dishDto) {
        service.updateWithFlavor(dishDto);
        return R.success("Successfully update");
    }

    @DeleteMapping
    public R<String> delete(String ids) {
        log.info("Delete ids: {}", ids);
        service.deleteWithFlavor(ids);
        return R.success("Successfully delete");
    }

    /**
     * 根据categoryID获取菜品信息并返回
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List> select(Dish dish) {

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId, dish.getCategoryId());
        wrapper.eq(dish.getStatus() != null, Dish::getStatus, dish.getStatus());
        wrapper.orderByDesc(Dish::getUpdateTime);
        List<Dish> list = service.list(wrapper);
        List<DishDto> dishDtos = new ArrayList<>();

        for (Dish val : list) {
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(val, dto);
            LambdaQueryWrapper<DishFlavor> flavorWrapper = new LambdaQueryWrapper<>();
            flavorWrapper.eq(DishFlavor::getDishId, val.getId());
            List<DishFlavor> dishFlavors = flavorService.list(flavorWrapper);
            dto.setFlavors(dishFlavors);
            dishDtos.add(dto);
        }

        return R.success(dishDtos);
    }

}
