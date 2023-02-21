package com.zomkc.product.dao;

import com.zomkc.product.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌
 * 
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-15 15:27:37
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {

    void updateStatusById(Integer showStatus,Long brandId);
}
