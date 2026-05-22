package com.kuit.kuit4serverauth.resolver;

import com.kuit.kuit4serverauth.annotation.LoginUser;
import com.kuit.kuit4serverauth.model.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(org.springframework.core.MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        // Interceptor에서 setAttribute로 저장한 username, role을 꺼내옴
        HttpServletRequest request = (HttpServletRequest)  webRequest.getNativeRequest();
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");
        return new AuthUser(username, role);
    }
}
