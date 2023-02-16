package com.zomkc.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.coupon.entity.HomeSubjectEntity;

import java.util.Map;

/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-16 08:25:57
 */
public interface HomeSubjectService extends IService<HomeSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

