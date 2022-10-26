package com.jdfcc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.dto.DishDto;
import com.jdfcc.reggie.entity.Category;
import com.jdfcc.reggie.entity.Dish;
import com.jdfcc.reggie.entity.DishFlavor;
import com.jdfcc.reggie.mapper.DishMapper;
import com.jdfcc.reggie.service.CategoryService;
import com.jdfcc.reggie.service.DishFlavorService;
import com.jdfcc.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper mapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 多表查询
     *
     * @param page
     * @param size
     * @param name
     * @return
     */
    @Override
    public R<Page> selectPageWithFlavor(int page, int size, String name) {
        Page<Dish> page1 = new Page(page, size);
        Page<DishDto> pageDto = new Page(page, size);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            wrapper.like(Dish::getName, name);
        }
        Page<Dish> val = mapper.selectPage(page1, wrapper);

        BeanUtils.copyProperties(val, pageDto, "records");//属性拷贝
        List<Dish> list = val.getRecords();
        List<DishDto> listDto = new ArrayList<>();
        for (Dish temp : list) {
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(temp, dto);//属性拷贝
            Long id = temp.getCategoryId();
            Category category = categoryService.getById(id);
            String categoryName = category.getName();
            dto.setCategoryName(categoryName);
            listDto.add(dto);
        }
        pageDto.setRecords(listDto);
        return R.success(pageDto);
//        BeanUtils.copyProperties(val, pageDto);
//        List<DishDto> dtoList = pageDto.getRecords();
//        List<DishDto> dtoList1 = new ArrayList<>();
//        for (DishDto temp : dtoList) {
//            Long id = temp.getCategoryId();
//            Category category = categoryService.getById(id);
//            String dtoName = category.getName();
//            temp.setName(dtoName);
//            dtoList1.add(temp);
//        }
//        pageDto.setRecords(dtoList1);
//        return R.success(pageDto);

    }

    @Override
    public R<String> stopSeal(String id) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Dish::getId, id);
        Dish dish = mapper.selectOne(wrapper);
        dish.setStatus(0);
        mapper.update(dish, wrapper);
        return R.success("Successfully set status=0");
    }

    @Override
    public R<String> beginSeal(String id) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper();
        String[] ids = id.split(",");
        for (int i = 0; i < ids.length; i++) {
            String val = ids[i];
            wrapper.eq(Dish::getId, val);
            Dish dish = mapper.select(val);
            dish.setStatus(1);
            mapper.update(dish, wrapper);
        }
        return R.success("Successfully set status=1");
    }

    /**
     * 新增菜品，同时保存对应口味数据
     *
     * @param dishDto
     * @return
     */

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
            //保存菜品口味数据到菜品口味表dish_flavor
            dishFlavorService.save(flavor);
        }
    }



    /**
     * 根据id查询对应菜品信息和口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);

        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 更新dish表以及flavor表
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto );
        LambdaQueryWrapper<DishFlavor> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        //根据id删除flavor表里面的数据
        dishFlavorService.remove(wrapper);

        //再将数据插入至flavor表
        List<DishFlavor> flavorList=dishDto.getFlavors();
        List<DishFlavor> flavorList1=new ArrayList<>();
        for(DishFlavor temp:flavorList){
            temp.setDishId(dishDto.getId());
            flavorList1.add(temp);
        }
        dishFlavorService.saveBatch(flavorList1);
    }

    @Override
    @Transactional
    public void deleteWithFlavor(Long ids) {
        //删除dish表里面的数据
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getId,ids);
        this.remove(dishLambdaQueryWrapper);
        //删除dishFlavor表里面的数据
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,ids);
        //获取集合并遍历删除
        List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        for(DishFlavor temp:flavors){
            dishFlavorService.removeById(temp.getId());
        }
    }
}


//#TODO: 批量方法均未完成