package com.zomkc.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-16 09:15:33
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

