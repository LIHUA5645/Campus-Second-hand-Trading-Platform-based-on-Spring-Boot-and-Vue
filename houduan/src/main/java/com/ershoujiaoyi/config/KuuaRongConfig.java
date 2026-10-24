// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class KuuaRongConfig implements WebMvcConfigurer {

    @Autowired
    private GuanLiYuanLanJieQi guanLiYuanLanJieQi;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(guanLiYuanLanJieQi).addPathPatterns("/admin/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
