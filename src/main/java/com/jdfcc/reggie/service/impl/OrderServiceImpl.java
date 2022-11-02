package com.jdfcc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdfcc.reggie.entity.Orders;
import com.jdfcc.reggie.mapper.OrderMapper;
import com.jdfcc.reggie.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
}
