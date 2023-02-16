package com.zomkc.coupon.dao;

import com.zomkc.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-16 08:25:58
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
