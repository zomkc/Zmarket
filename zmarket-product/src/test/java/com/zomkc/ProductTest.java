package com.zomkc;


import com.zomkc.product.ProductApplication;
import com.zomkc.product.dao.AttrGroupDao;
import com.zomkc.product.entity.BrandEntity;
import com.zomkc.product.entity.CategoryEntity;
import com.zomkc.product.service.BrandService;
import com.zomkc.product.service.CategoryService;
import com.zomkc.product.vo.SpuItemAttrGroupVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApplication.class)
public class ProductTest {

    @Autowired
    private AttrGroupDao attrGroupDao;
    @Test
    public void TestAttr(){
        List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(9L, 225L);
        System.out.println(attrGroupWithAttrsBySpuId);
    }

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redisClient(){
        System.out.println(redissonClient);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void RedisTest(){
        redisTemplate.opsForValue().set("ll","张三");

        Object o = redisTemplate.opsForValue().get("ll");
        System.out.println(o);

        redisTemplate.delete("ll");

    }



    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @Test
    public void testPatentPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        System.out.println(Arrays.asList(catelogPath));
    }


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
