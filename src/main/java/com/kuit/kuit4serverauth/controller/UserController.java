package com.kuit.kuit4serverauth.controller;

import com.kuit.kuit4serverauth.annotation.AuthUser;
import com.kuit.kuit4serverauth.model.LoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(@AuthUser LoginUser loginUser) {
        return ResponseEntity.ok("Hello, " + loginUser.getUsername());
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(@AuthUser LoginUser loginUser) {
        if ("ROLE_ADMIN".equals(loginUser.getRole())) {
            return ResponseEntity.ok("Hello, admin");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
    }
}
