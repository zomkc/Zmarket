package com.zomkc.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zomkc.product.entity.ProductAttrValueEntity;
import com.zomkc.product.service.ProductAttrValueService;
import com.zomkc.product.vo.AttrRespVo;
import com.zomkc.product.vo.AttrVo;
import constant.ProductConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zomkc.product.entity.AttrEntity;
import com.zomkc.product.service.AttrService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.common.utils.R;



/**
 * 商品属性
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-15 15:27:36
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueService productAttrValueService;

    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrlistforspu(@PathVariable("spuId") Long spuId){

        List<ProductAttrValueEntity> entities = productAttrValueService.baseAttrlistforspu(spuId);

        return R.ok().put("data",entities);
    }

    //规格参数 1
    @GetMapping("/base/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String,Object> params, @PathVariable("catelogId") Long catelogId){
        //0是销售属性,1是属性规格
        PageUtils page =  attrService.queryBaseAttrPage(params,catelogId,ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        return R.ok().put("page",page);
    }
    //销售属性 0
    @GetMapping("/sale/list/{catelogId}")
    public R saleAttrList(@RequestParam Map<String,Object> params, @PathVariable("catelogId") Long catelogId){
        PageUtils page =  attrService.queryBaseAttrPage(params,catelogId, ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        return R.ok().put("page",page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);
        AttrRespVo attr = attrService.getByIdInfo(attrId);
        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,
                           @RequestBody List<ProductAttrValueEntity> entities){

        productAttrValueService.updateSpuAttr(spuId,entities);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
