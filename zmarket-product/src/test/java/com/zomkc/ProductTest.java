package com.zomkc;

import com.aliyun.oss.*;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.zomkc.product.ProductApplication;
import com.zomkc.product.dao.CategoryDao;
import com.zomkc.product.entity.BrandEntity;
import com.zomkc.product.entity.CategoryEntity;
import com.zomkc.product.service.BrandService;
import com.zomkc.product.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApplication.class)
public class ProductTest {

    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;


    @Test
    public void test(){
        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("华为");
        brandEntity.setBrandId(1L);
//        brandService.save(brandEntity);
        System.out.println(brandService.getById(brandEntity));
    }

    @Test
    public void testCategory(){
        List<CategoryEntity> entities = categoryService.listWithTree();
        entities.forEach(s -> System.out.println(s));
    }
}
