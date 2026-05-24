package com.kuit.kuit4serverauth.controller;

import com.kuit.kuit4serverauth.auth.AuthUser;
import com.kuit.kuit4serverauth.auth.CurrentUser;
import com.kuit.kuit4serverauth.exception.CustomException;
import com.kuit.kuit4serverauth.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<AuthUser> getProfile(@CurrentUser AuthUser user) {
        return ResponseEntity.ok(user);
    }

    @GetMapping("/admin")
    public ResponseEntity<AuthUser> getAdmin(@CurrentUser AuthUser user) {
        if (!user.isAdmin()) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }
        return ResponseEntity.ok(user);
    }
}
