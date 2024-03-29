package com.zomkc.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.product.entity.CategoryEntity;
import com.zomkc.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-15 15:27:37
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    //组织groupId完整路径   [父/子/孙]
    Long[] findCatelogPath(Long groupId);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getLevel1Catagorys();

    Map<String, List<Catelog2Vo>> getCatalogJson();
}

