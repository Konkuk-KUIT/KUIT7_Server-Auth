package com.kuit.kuit4serverauth.controller;

import com.kuit.kuit4serverauth.exception.CustomException;
import com.kuit.kuit4serverauth.exception.ErrorCode;
import com.kuit.kuit4serverauth.resolver.AuthUser;
import com.kuit.kuit4serverauth.resolver.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(@LoginUser AuthUser user) {
        return ResponseEntity.ok("Hello, " + user.getUsername());
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(@LoginUser AuthUser user) {
        if (!ROLE_ADMIN.equals(user.getRole())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }
        return ResponseEntity.ok("Hello, admin");
    }
}
