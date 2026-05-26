package com.kuit.kuit4serverauth.config;

import com.kuit.kuit4serverauth.LoginRoleArgumentResolver;
import com.kuit.kuit4serverauth.LoginUserArgumentResolver;
import com.kuit.kuit4serverauth.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final LoginRoleArgumentResolver loginRoleArgumentResolver;

    public WebConfig(AuthInterceptor authInterceptor, LoginUserArgumentResolver loginUserArgumentResolver, LoginRoleArgumentResolver loginRoleArgumentResolver) {
        this.authInterceptor = authInterceptor;
        this.loginUserArgumentResolver = loginUserArgumentResolver;
        this.loginRoleArgumentResolver = loginRoleArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/profile","/admin");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
        resolvers.add(loginRoleArgumentResolver);
    }


}
