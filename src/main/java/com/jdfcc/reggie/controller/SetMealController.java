package com.jdfcc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.dto.SetmealDto;
import com.jdfcc.reggie.entity.Setmeal;
import com.jdfcc.reggie.entity.SetmealDish;
import com.jdfcc.reggie.service.SetMealDishService;
import com.jdfcc.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService service;

    @Autowired
    private SetMealDishService dishService;


    @GetMapping("/page")
    public R<Page<SetmealDto>> selectPage(int page, int pageSize, String name) {
        Page<SetmealDto> dtoPage = service.selectWithCategory(page, pageSize, name);
        return R.success(dtoPage);
    }

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        service.saveWithCategory(setmealDto);
        return R.success("Successfully save");
    }

    /**
     * 返回单个dto
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> select(@PathVariable Long id) {
        LambdaQueryWrapper<SetmealDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDto::getId, id);
        Setmeal setmeal = service.getById(id);
        Long SetMal_id = setmeal.getId();
        LambdaQueryWrapper<SetmealDish> dishWrapper = new LambdaQueryWrapper<>();
        dishWrapper.eq(SetmealDish::getSetmealId, SetMal_id);
        List<SetmealDish> list = dishService.list(dishWrapper);
        SetmealDto dto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, dto);
        dto.setSetmealDishes(list);
        return R.success(dto);
    }

    /**
     * 修改套餐信息
     *
     * @param dto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto dto) {
        service.update(dto);
        return R.success("Successfully update");
    }

    @DeleteMapping
    public R<String> delete(String ids) {
        String[] vals = ids.split(",");
        for (String temp : vals) {
            service.delete(temp);
        }
        return R.success("Successfully delete");
    }

    @PostMapping("/status/{status}")
    public R<String> SetStatus(@PathVariable int status, String ids) {
        log.info("Id: {},Status: {}", ids, status);
        String[] vals = ids.split(",");
        for (String val : vals)
            service.setStatus(status, val);
        return R.success("Successfully update");
    }
}
