package com.jdfcc.reggie.controller;

import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.ShoppingCart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {


        return null;
    }

}
