package com.kuit.kuit4serverauth.controller;

import com.kuit.kuit4serverauth.exception.CustomException;
import com.kuit.kuit4serverauth.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(HttpServletRequest request) {
        // TODO : 로그인 한 사용자면 username 이용해 "Hello, {username}" 반환하기
        // 1. 인터셉터가 request 주머니에 넣어둔 'username'을 꺼냄
        String username = (String) request.getAttribute("username");

        // 2. 요구사항에 맞춰 "Hello, {username}" 반환
        return ResponseEntity.ok("Hello, " + username);
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(HttpServletRequest request) {
        // TODO: role이 admin이면 "Hello, admin" 반환하기
        // 1. 인터셉터가 request 주머니에 넣어둔 'role'을 꺼냄
        String role = (String) request.getAttribute("role");

        // 2. 권한이 ROLE_ADMIN이 아니라면 CustomException 발생시켜서 차단!
        if (!"ROLE_ADMIN".equals(role)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 3. 무사히 통과했다면 관리자 환영 인사 반환
        return ResponseEntity.ok("Hello, admin");
    }
}
