package com.jdfcc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdfcc.reggie.dto.DishDto;
import com.jdfcc.reggie.dto.SetmealDto;
import com.jdfcc.reggie.entity.Category;
import com.jdfcc.reggie.entity.Dish;
import com.jdfcc.reggie.entity.Setmeal;
import com.jdfcc.reggie.entity.SetmealDish;
import com.jdfcc.reggie.mapper.CategoryMapper;
import com.jdfcc.reggie.mapper.SetmealMapper;
import com.jdfcc.reggie.service.CategoryService;
import com.jdfcc.reggie.service.SetMealDishService;
import com.jdfcc.reggie.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetMealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetMealDishService setMealDishService;

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    @Transactional
    public Page<SetmealDto> selectWithCategory(int page, int pageSize, String name) {
        Page<Setmeal> PageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        Page listPage = setmealMapper.selectPage(PageInfo, setmealLambdaQueryWrapper);

        BeanUtils.copyProperties(listPage, setmealDtoPage, "records");

        List<Setmeal> setmealList = listPage.getRecords();
        List<SetmealDto> setmealDtoList = new ArrayList<>();

        for (Setmeal temp : setmealList) {
//            复制属性
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(temp, setmealDto);

            Long category_id = temp.getCategoryId();
            Category category = categoryService.getById(category_id);
//            设置Dto.CategoryName
            String CatName = category.getName();
            setmealDto.setCategoryName(CatName);
//            获取套餐内容集合并填充
            LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, temp.getId());
            List<SetmealDish> setmealDishList = setMealDishService.list(dishLambdaQueryWrapper);
            setmealDto.setSetmealDishes(setmealDishList);
            setmealDtoList.add(setmealDto);
        }
        setmealDtoPage.setRecords(setmealDtoList);
        return setmealDtoPage;
    }


    @Override
    @Transactional
    public void saveWithCategory(SetmealDto dto) {
        this.save(dto);
        List<SetmealDish> dishList = dto.getSetmealDishes();
        Long id = dto.getId();

        for (SetmealDish temp : dishList) {
            temp.setSetmealId(id);
            setMealDishService.save(temp);
        }

    }

    @Override
    @Transactional
    public void update(SetmealDto dto) {
        this.updateById(dto);
        Long id = dto.getId();

        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        setMealDishService.remove(dishLambdaQueryWrapper);

        List<SetmealDish> list = dto.getSetmealDishes();
        List<SetmealDish> list1 = new ArrayList<>();
        for (SetmealDish temp : list) {
            temp.setSetmealId(id);
            list1.add(temp);
        }
        setMealDishService.saveBatch(list1);
    }

    @Override
    @Transactional
    public void delete(String id) {
        this.removeById(id);

        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        setMealDishService.remove(dishLambdaQueryWrapper);

    }

    @Override
    @Transactional
    public void setStatus(int status, String id) {
//        LambdaQueryWrapper<Setmeal> wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq(Setmeal::getId,id);
//        setmealMapper.selectOne(wrapper).setStatus(status);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Setmeal::getId, id);
        Setmeal setmeal = setmealMapper.selectOne(wrapper);
        setmeal.setStatus(status);
        setmealMapper.update(setmeal, wrapper);
    }

}
