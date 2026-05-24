package com.kuit.kuit4serverauth.auth;

public record AuthUser(String username, String role) {

    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(role);
    }
}
