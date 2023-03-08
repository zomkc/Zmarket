package com.zomkc.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-16 09:25:39
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);
}

