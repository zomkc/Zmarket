package com.zomkc.ware.dao;

import com.zomkc.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-16 09:25:39
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
