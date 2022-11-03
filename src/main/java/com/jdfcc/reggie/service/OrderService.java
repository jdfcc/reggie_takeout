package com.jdfcc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jdfcc.reggie.dto.OrdersDto;
import com.jdfcc.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {

    public void submit(Orders orders);

    public Page<OrdersDto> orderPage(int page,int pageSize);

}
