package com.zomkc.product.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zomkc.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.common.utils.Query;

import com.zomkc.product.dao.CategoryDao;
import com.zomkc.product.entity.CategoryEntity;
import com.zomkc.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //baseMapper.listWithTree(); //baseMapper已经把CategoryDao注入进来了
        //查出所有分类
        List<CategoryEntity> categorys = categoryDao.listWithTree();
        //组装父子结构
        List<CategoryEntity> Level1Menus = categorys.stream()
                .filter(CategoryEntity -> CategoryEntity.getParentCid() == 0)
                .map((menu) -> {
                    menu.setChildren(getChildrens(menu,categorys));
                    return menu;
                })
                .sorted((menu1,menu2) -> { return menu1.getSort() - menu2.getSort(); })
//                .sorted((Comparator.comparing(CategoryEntity::getSort))
                .collect(Collectors.toList());


        return Level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1.检测当前菜单是否在别的地方引用

        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long groupId) {
        List<Long> path = new ArrayList<>();
        List<Long> parentPath = findParentPath(groupId, path);
        Collections.reverse(parentPath);    //逆序转换父子顺序
        return parentPath.toArray(new Long[0]);
    }

    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    private List<Long> findParentPath(Long groupId, List<Long> path){
        //收集当前节点
        path.add(groupId);
        CategoryEntity category = this.getById(groupId);
        if (category.getParentCid() != 0){
            //收集当前节点父节点
            findParentPath(category.getParentCid(),path);
        }
        return path;
    }

    //传入当前菜单,和所有菜单, 递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> collect = all.stream().filter(CategoryEntity -> {
            //为当前菜单找到所有子菜单 在所有菜单中过滤出当前菜单的子菜单
            return CategoryEntity.getParentCid().equals(root.getCatId());
        }).map(CategoryEntity -> {
            //递归 为过滤出来的子菜单找出子菜单的所有分类
            CategoryEntity.setChildren(getChildrens(CategoryEntity,all));
            return CategoryEntity;
        })
        .sorted((menu1,menu2) -> {
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        })
        .collect(Collectors.toList());
        return collect;
    }
}