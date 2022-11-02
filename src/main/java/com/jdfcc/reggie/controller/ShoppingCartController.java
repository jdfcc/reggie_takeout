package com.jdfcc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jdfcc.reggie.common.BaseContext;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.ShoppingCart;
import com.jdfcc.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 查询购物车所有数据
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list();
        return R.success(shoppingCarts);
    }

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        lambdaQueryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        ShoppingCart cart = shoppingCartService.getOne(lambdaQueryWrapper);
        if (cart != null) {
            Integer num = cart.getNumber();
            cart.setNumber(num + 1);
            shoppingCartService.updateById(cart);
        } else
            shoppingCartService.save(shoppingCart);
        return R.success("Add ok");
    }

    /**
     * 购物车删除单个
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<String> removeOne(@RequestBody ShoppingCart shoppingCart) {
        log.info("Delete shoppingCart: {}", shoppingCart.toString());
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        wrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        ShoppingCart cart = shoppingCartService.getOne(wrapper);//获得待删除对象

        if(cart.getNumber()==1)
            shoppingCartService.remove(wrapper);

        cart.setNumber(cart.getNumber()-1);
        shoppingCartService.updateById(cart);
        return R.success("Delete ok");
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> removeAll(){
        Long id=BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,id);
        shoppingCartService.remove(wrapper);
        return R.success("Clear ok");
    }

}
