package com.zomkc.product.service.impl;

import com.zomkc.product.dao.BrandDao;
import com.zomkc.product.dao.CategoryDao;
import com.zomkc.product.entity.BrandEntity;
import com.zomkc.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.common.utils.Query;

import com.zomkc.product.dao.CategoryBrandRelationDao;
import com.zomkc.product.entity.CategoryBrandRelationEntity;
import com.zomkc.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setBrandId(brandId);
        entity.setBrandName(name);
        this.update(entity,new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",entity.getBrandId()));
    }

    @Override
    public void updateCategory(Long catId, String name) {
//        brandDao.updateCategory(catId,name);
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setCatelogId(catId);
        entity.setCatelogName(name);
        this.update(entity,new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id",entity.getCatelogId()));
    }

}