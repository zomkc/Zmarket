package com.zomkc.product.dao;

import com.zomkc.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-15 15:27:37
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
