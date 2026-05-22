package com.kuit.kuit4serverauth.config;

import com.kuit.kuit4serverauth.interceptor.AuthInterceptor;
import com.kuit.kuit4serverauth.resolver.LoginUserResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final LoginUserResolver loginUserResolver;

    public WebConfig(AuthInterceptor authInterceptor, LoginUserResolver loginUserResolver) {
        this.authInterceptor = authInterceptor;
        this.loginUserResolver = loginUserResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO /profile, /admin 앞에 붙이기
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/profile", "/admin");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserResolver);
    }
}
