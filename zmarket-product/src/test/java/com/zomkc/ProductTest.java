package com.zomkc;

import com.zomkc.product.ProductApplication;
import com.zomkc.product.entity.BrandEntity;
import com.zomkc.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApplication.class)
public class ProductTest {

    @Autowired
    private BrandService brandService;

    @Test
    public void test(){
        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("华为");
        brandEntity.setBrandId(1L);
//        brandService.save(brandEntity);
        System.out.println(brandService.getById(brandEntity));
    }

}
