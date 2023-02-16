package com.zomkc.order.dao;

import com.zomkc.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-16 09:15:33
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
