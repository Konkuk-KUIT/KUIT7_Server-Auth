package com.kuit.kuit4serverauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUser {
    private Long userId;
    private String username;
    private String role;
}
