package com.jdfcc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.dto.OrdersDto;
import com.jdfcc.reggie.entity.OrderDetail;
import com.jdfcc.reggie.entity.Orders;
import com.jdfcc.reggie.service.OrderDetailService;
import com.jdfcc.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("OK");
    }

    /**
     * 查看用户订单
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> orderPage(int page, int pageSize) {
        Page<OrdersDto> dtoPage = orderService.orderPage(page, pageSize);
        return R.success(dtoPage);
    }

    /**
     * 后台查看订单
     * @param page
     * @param pageSize
     * @return
     */

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        Page page1 = new Page(page, pageSize);
        Page page2 = orderService.page(page1);
        return R.success(page2);
    }



}
