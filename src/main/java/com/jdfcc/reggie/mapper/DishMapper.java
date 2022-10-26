package com.jdfcc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jdfcc.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    @Select(" select * from dish where id=#{id}")
    Dish select(@Param("id") String id);
}
