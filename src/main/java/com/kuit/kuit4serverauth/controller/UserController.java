package com.kuit.kuit4serverauth.controller;

import com.kuit.kuit4serverauth.annotation.LoginUser;
import com.kuit.kuit4serverauth.model.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(@LoginUser AuthUser authUser) {
        // TODO : 로그인 한 사용자면 username 이용해 "Hello, {username}" 반환하기
        String username = authUser.getUsername();
        if (username != null)
            return ResponseEntity.ok("Hello, " + username);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(@LoginUser AuthUser authUser) {
        // TODO: role이 admin이면 "Hello, admin" 반환하기
        String role = authUser.getRole();
        if (role != null && role.equals("ROLE_ADMIN"))
            return ResponseEntity.ok("Hello, admin");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
    }
}
