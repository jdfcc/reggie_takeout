package com.jdfcc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.dto.DishDto;
import com.jdfcc.reggie.dto.SetmealDto;
import com.jdfcc.reggie.entity.*;
import com.jdfcc.reggie.service.CategoryService;
import com.jdfcc.reggie.service.DishFlavorService;
import com.jdfcc.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService service;

    @Autowired
    private DishFlavorService flavorService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> selectPage(int page, int pageSize, String name) {
        R r = service.selectPageWithFlavor(page, pageSize, name);
        return r;
    }

    @PostMapping("/status/{status}")
    public R<String> setStatusOff(String ids, @PathVariable String status) {
        log.info("Set status: {}", status);
        String id = String.valueOf(ids);
        if (id.contains(",")) {
//            List<Long> list = new ArrayList<>();
            log.info("many");
        }
//        log.info(String.valueOf(ids));
        R r = service.setStatus(ids, status);
        Set keys=redisTemplate.keys("dishId_*");
        redisTemplate.delete(keys);
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
        Set keys=redisTemplate.keys("dishId*");
        redisTemplate.delete(keys);
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
        Set keys=redisTemplate.keys("dishId*");
        redisTemplate.delete(keys);
        service.updateWithFlavor(dishDto);
        return R.success("Successfully update");
    }

    @DeleteMapping
    public R<String> delete(String ids) {
        log.info("Delete ids: {}", ids);
        Set keys=redisTemplate.keys("dishId*");
        redisTemplate.delete(keys);
        service.deleteWithFlavor(ids);
        return R.success("Successfully delete");
    }

    /**
     * 根据categoryID获取菜品信息并返回
     *
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List> select(Dish dish) {
//
//        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId, dish.getCategoryId());
//        wrapper.eq(dish.getStatus() != null, Dish::getStatus, dish.getStatus());
//        wrapper.orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = service.list(wrapper);
//        List<DishDto> dishDtos = new ArrayList<>();
//
//
//        for (Dish val : list) {
//            DishDto dto = new DishDto();
//            BeanUtils.copyProperties(val, dto);
//            LambdaQueryWrapper<DishFlavor> flavorWrapper = new LambdaQueryWrapper<>();
//            flavorWrapper.eq(DishFlavor::getDishId, val.getId());
//            List<DishFlavor> dishFlavors = flavorService.list(flavorWrapper);
//            dto.setFlavors(dishFlavors);
//            dishDtos.add(dto);
//        }
//
//        return R.success(dishDtos);
//    }
    @GetMapping("/list")
    public R<List> select(Dish dish) {
        List<DishDto> Temp = new ArrayList<>();

        //若能在redis缓存中找到此数据，则直接返回
        String key = "dishId_" + dish.getCategoryId() + "_" + dish.getStatus();
        Temp = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (Temp != null) {
            return R.success(Temp);
        }

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
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

        //查找并储存进redis缓存
        redisTemplate.opsForValue().set(key, dishDtos, 1, TimeUnit.HOURS);
        return R.success(dishDtos);
    }


//    @GetMapping("/list")
//    public R<List<DishDto>> list(Dish dish){
//        List<DishDto> dishDtoList = null;
//
//        //动态构造key
//        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();//dish_1397844391040167938_1
//
//        //先从redis中获取缓存数据
//        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
//
//        if(dishDtoList != null){
//            //如果存在，直接返回，无需查询数据库
//            return R.success(dishDtoList);
//        }
//
//        //构造查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
//        //添加条件，查询状态为1（起售状态）的菜品
//        queryWrapper.eq(Dish::getStatus,1);
//
//        //添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = service.list(queryWrapper);
//
//        dishDtoList = list.stream().map((item) -> {
//            DishDto dishDto = new DishDto();
//
//            BeanUtils.copyProperties(item,dishDto);
//
//            Long categoryId = item.getCategoryId();//分类id
//            //根据id查询分类对象
//            Category category = categoryService.getById(categoryId);
//
//            if(category != null){
//                String categoryName = category.getName();
//                dishDto.setCategoryName(categoryName);
//            }
//
//            //当前菜品的id
//            Long dishId = item.getId();
//            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
//            //SQL:select * from dish_flavor where dish_id = ?
//            List<DishFlavor> dishFlavorList = flavorService.list(lambdaQueryWrapper);
//            dishDto.setFlavors(dishFlavorList);
//            return dishDto;
//        }).collect(Collectors.toList());
//
//        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis
//        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
//
//        return R.success(dishDtoList);
//    }

}
