package com.wenda.configuration;

import com.wenda.interceptor.LoginRequiredInterceptor;
import com.wenda.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Create by xrh
 * 3:34 PM on 12/5/19 2019
 */
@Component
public class WendaWebConfiguration implements WebMvcConfigurer {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    //配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        //配置拦截器顺序不可变，因为loginRequiredInterceptor用到了passportInterceptor配置的hostHander
        //未登录不能访问用户界面
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
    }

}
