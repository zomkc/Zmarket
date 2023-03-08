package com.zomkc.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.ware.entity.PurchaseEntity;
import com.zomkc.ware.vo.MergeVo;
import com.zomkc.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-16 09:25:39
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);
}

