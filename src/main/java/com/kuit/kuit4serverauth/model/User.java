package com.kuit.kuit4serverauth.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String username;
    private String password;
    private String role; // e.g., ROLE_USER, ROLE_ADMIN
}