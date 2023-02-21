/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.zomkc.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        //配置跨域 Access-Control-Allow-Origin
        configuration.addAllowedHeader("*");
        //允许哪些请求来源跨域
        configuration.addAllowedOrigin("*");
        //允许哪些请求跨域
        configuration.addAllowedMethod("*");
        //是否允许携带cookie跨域
        configuration.setAllowCredentials(true);
        //任意路径都进行跨域配置,跨域配置
        source.registerCorsConfiguration("/**", configuration);
        return new CorsWebFilter(source);
    }

}