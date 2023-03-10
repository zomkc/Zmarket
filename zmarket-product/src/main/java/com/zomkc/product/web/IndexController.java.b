package com.zomkc.product.web;

import com.zomkc.common.utils.R;
import com.zomkc.product.entity.CategoryEntity;
import com.zomkc.product.service.CategoryService;
import com.zomkc.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/product")
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R IndexPage(){
        //拿出所有一级分类
        List<CategoryEntity> category = categoryService.getLeveliCategorys();
        return R.ok().put("data",category);
    }

    @GetMapping("/index/catalog.json")
    public Map<String,List<Catelog2Vo>> getCatelogJson(){
        Map<String,List<Catelog2Vo>> json = categoryService.getCatelogJson();
        return json;
    }
}
