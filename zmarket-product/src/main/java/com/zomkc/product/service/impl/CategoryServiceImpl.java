package com.zomkc.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zomkc.product.service.CategoryBrandRelationService;
import com.zomkc.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
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
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    private StringRedisTemplate redisTemplate;

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

    @Override
    public List<CategoryEntity> getLeveliCatagorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }


    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (!StringUtils.isEmpty(catalogJson)){ //不是空就返回
//            Map<String, List<Catelog2Vo>> map = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>(){
//            });
        return JSON.parseObject(catalogJson,Map.class);
        }
        //查数据库
        Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDb();
            String jsonString = JSON.toJSONString(catalogJsonFromDb);
            //存入redis
            redisTemplate.opsForValue().set("catalogJson",jsonString,1, TimeUnit.DAYS);
        return  catalogJsonFromDb;
    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDb() {
        List<CategoryEntity> selectList = this.baseMapper.selectList(null);

        //1、查出所有分类
        //1、1）查出所有一级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        //封装数据
        Map<String, List<Catelog2Vo>> parentCid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1、每一个的一级分类,查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());

            //2、封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName().toString());

                    //1、找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());

                    if (level3Catelog != null) {
                        List<Catelog2Vo.Category3Vo> category3Vos = level3Catelog.stream().map(l3 -> {
                            //2、封装成指定格式
                            Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                            return category3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(category3Vos);
                    }

                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));

        return parentCid;
    }




    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList,Long parentCid) {
        List<CategoryEntity> categoryEntities = selectList.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
        return categoryEntities;
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