package com.jdfcc.reggie.dto;

import com.jdfcc.reggie.entity.Setmeal;
import com.jdfcc.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
