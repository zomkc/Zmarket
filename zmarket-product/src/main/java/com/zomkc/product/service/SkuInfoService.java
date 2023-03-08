package com.zomkc.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-15 15:27:36
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

