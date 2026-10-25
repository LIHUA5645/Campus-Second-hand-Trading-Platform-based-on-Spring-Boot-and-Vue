// 作者：肖沐樑 QQ：2047419378
package com.ershoujiaoyi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 把上传目录映射为 /uploads/**，前台可直接用 <img src="/uploads/lunbo/xxx.jpg"> 访问。
 */
@Configuration
public class WenJianZiYuanConfig implements WebMvcConfigurer {

    @Value("${ershou.upload-dir:upload}")
    private String shangChuanMuLu;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String jueDui = new File(shangChuanMuLu).getAbsolutePath();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + jueDui + File.separator);
    }
}
