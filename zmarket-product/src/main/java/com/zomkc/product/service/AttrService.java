package com.zomkc.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.product.entity.AttrEntity;
import com.zomkc.product.vo.AttrRespVo;
import com.zomkc.product.vo.AttrVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-15 15:27:36
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId,int type);

    AttrRespVo getByIdInfo(Long attrId);

    void updateAttr(AttrVo attr);
}

