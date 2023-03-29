package com.zomkc.product.feign;


import com.zomkc.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



//@FeignClient(value = "gulimall-seckill",fallback = SeckillFeignServiceFallBack.class)
@FeignClient(value = "zmarket-seckill")
public interface SeckillFeignService {

    /**
     * 根据skuId查询商品是否参加秒杀活动
     * @param skuId
     * @return
     */
    @GetMapping(value = "/sku/seckill/{skuId}")
    R getSkuSeckilInfo(@PathVariable("skuId") Long skuId);

}
