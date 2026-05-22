package com.kuit.kuit4serverauth.model;

import lombok.Getter;

@Getter
public class AuthUser {
    private String username;
    private String role;

    public AuthUser(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
