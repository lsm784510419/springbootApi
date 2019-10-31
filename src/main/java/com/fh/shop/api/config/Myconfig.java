package com.fh.shop.api.config;

import com.fh.shop.api.common.SpringArgumentResolver;
import com.fh.shop.api.interceptor.IdempotentInterceptor;
import com.fh.shop.api.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;
//拦截器
@Configuration
public class Myconfig extends WebMvcConfigurerAdapter {
    //配置拦截器    相当于beans标签
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(idempotentInterceptor()).addPathPatterns("/**");
    }
    //配置spring解析器
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(springArgumentResolver());
    }
    //将ben标签放入上边的beans标签内，
    @Bean
    public LoginInterceptor loginInterceptor(){

        return new LoginInterceptor();
    }
    @Bean
    public IdempotentInterceptor idempotentInterceptor(){

        return new IdempotentInterceptor();
    }

    @Bean
    public SpringArgumentResolver springArgumentResolver(){

        return new SpringArgumentResolver();
    }

}
