package com.jdfcc.reggie.dto;

import com.jdfcc.reggie.entity.Dish;
import com.jdfcc.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
