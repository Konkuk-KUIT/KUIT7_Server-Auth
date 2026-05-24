package com.kuit.kuit4serverauth.service;

import com.kuit.kuit4serverauth.exception.CustomException;
import com.kuit.kuit4serverauth.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "myverylongsecretkeyforjwtauthenticationsystemthatisverysecure";
    private final long accessTokenExpirationMs = 3600000;
    private final long refreshTokenExpirationMs = 1209600000;

    public String generateToken(String username, String role) {
        return generateAccessToken(username, role);
    }

    public String generateAccessToken(String username, String role) {
        return generateToken(username, role, "access", accessTokenExpirationMs);
    }

    public String generateRefreshToken(String username, String role) {
        return generateToken(username, role, "refresh", refreshTokenExpirationMs);
    }

    private String generateToken(String username, String role, String type, long expirationMs) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("type", type)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Claims validateRefreshToken(String token) {
        Claims claims = validateToken(token);
        if (!"refresh".equals(claims.get("type", String.class))) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return claims;
    }
}
