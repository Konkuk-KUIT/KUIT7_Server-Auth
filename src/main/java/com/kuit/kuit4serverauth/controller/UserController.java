package com.kuit.kuit4serverauth.controller;

import com.kuit.kuit4serverauth.LoginRole;
import com.kuit.kuit4serverauth.LoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(@LoginUser String username) {
       if (username != null){
           String s = "Hello " + username;
           return ResponseEntity.ok(s);
       }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(@LoginRole String role) {
        if ("ROLE_ADMIN".equals(role)){
            String s = "Hello" + role;
            return ResponseEntity.ok(s);
        }
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
    }
}
