package com.zomkc.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.coupon.entity.SkuFullReductionEntity;
import to.SkuReductionTo;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-16 08:25:57
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(SkuReductionTo skuReductionTo);
}

