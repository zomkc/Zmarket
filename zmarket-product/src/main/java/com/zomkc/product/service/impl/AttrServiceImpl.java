package com.zomkc.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zomkc.product.dao.AttrAttrgroupRelationDao;
import com.zomkc.product.dao.AttrGroupDao;
import com.zomkc.product.dao.CategoryDao;
import com.zomkc.product.entity.AttrAttrgroupRelationEntity;
import com.zomkc.product.entity.AttrGroupEntity;
import com.zomkc.product.entity.CategoryEntity;
import com.zomkc.product.service.CategoryService;
import com.zomkc.product.vo.AttrGroupRelationVo;
import com.zomkc.product.vo.AttrRespVo;
import com.zomkc.product.vo.AttrVo;
import constant.ProductConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.common.utils.Query;

import com.zomkc.product.dao.AttrDao;
import com.zomkc.product.entity.AttrEntity;
import com.zomkc.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        //??????????????????
        this.save(attrEntity);
        //?????????????????????
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(relationEntity);
        }

    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId,int type) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>();
        wrapper.eq("attr_type",type);
        if (catelogId != 0){
            wrapper.eq("catelog_id",catelogId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)){
            wrapper.and((wp) -> {
                wp.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            if (type == 1){
                AttrAttrgroupRelationEntity attrByid = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrByid != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrByid.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getByIdInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,attrRespVo);
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //??????????????????
            AttrAttrgroupRelationEntity attrByid = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (attrByid != null){
                attrRespVo.setAttrGroupId(attrByid.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrByid.getAttrGroupId());
                if (attrGroupEntity != null){
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }
        //??????????????????
        Long catelogId = attrRespVo.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrRespVo.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null){
            attrRespVo.setCatelogName(categoryEntity.getName());
        }

        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //??????????????????
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attr.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationDao.update(relationEntity,new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
        }

    }
    //????????????id????????????????????????
    @Override
    public List<AttrEntity> getRelationList(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));

        List<Long> attrIds = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        if(attrIds == null || attrIds.size() == 0){
            return null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        List<AttrAttrgroupRelationEntity> entitys = Arrays.asList(vos).stream().map(item -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(entitys);
    }
    //?????????????????????????????????????????????
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //???????????????????????????????????????????????????????????????
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //?????????????????????????????????????????????????????????
        //??????????????????????????????
        List<AttrGroupEntity> groupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));

        //??????????????????attrGroupId
        List<Long> collect = groupEntities.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());


        //???????????????????????????
        List<AttrAttrgroupRelationEntity> groupId = relationDao.selectList
                (new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));

        List<Long> attrIds = groupId.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //????????????????????????????????????????????????
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());

        if (attrIds != null && attrIds.size() > 0) {
            queryWrapper.notIn("attr_id", attrIds);
        }

        //???????????????????????????????????????
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((w) -> {
                w.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);

        PageUtils pageUtils = new PageUtils(page);


        return pageUtils;
    }

    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {
        List<Long> searchAttrIds = this.baseMapper.selectSearchAttrIds(attrIds);
        return searchAttrIds;
    }

}