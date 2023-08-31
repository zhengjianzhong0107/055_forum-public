package com.milanuo.springboot2mybatisforum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author starsea
 * @date 2021-01-22
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photo/user/**").addResourceLocations("file:E:\\educationProject\\论坛系统\\springboot\\forum-master\\src\\main\\resources\\static\\photo\\user\\");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
