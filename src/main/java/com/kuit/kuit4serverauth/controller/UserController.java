package com.kuit.kuit4serverauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(HttpServletRequest request) {
        // TODO : 로그인 한 사용자면 username 이용해 "Hello, {username}" 반환하기

       String username = (String) request.getAttribute("username");
       if (username != null){
           String s = "Hello " + username;
           return ResponseEntity.status(HttpStatus.FOUND).body(s);
       }

        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(HttpServletRequest request) {
        // TODO: role이 admin이면 "Hello, admin" 반환하기

        String role = (String)request.getAttribute("role");
        if ("admin".equals(role)){
            String s = "Hello" + role;
            return ResponseEntity.status(HttpStatus.FOUND).body(s);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
    }
}
