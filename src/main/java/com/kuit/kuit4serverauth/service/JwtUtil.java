package com.kuit.kuit4serverauth.service;

import com.kuit.kuit4serverauth.exception.CustomException;
import com.kuit.kuit4serverauth.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "mysecretkeymysecretkeymysecretkeymysecretkey";
    private final long accessExpirationMs = 3600000;        // 1 hour
    private final long refreshExpirationMs = 1209600000L;   // 14 days

    public String generateToken(String username, String role) {
        return generateAccessToken(username, role);
    }

    public String generateAccessToken(String username, String role) {
        return buildToken(username, role, accessExpirationMs, "access");
    }

    public String generateRefreshToken(String username, String role) {
        return buildToken(username, role, refreshExpirationMs, "refresh");
    }

    private String buildToken(String username, String role, long expirationMs, String type) {
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
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Claims validateRefreshToken(String token) {
        Claims claims = validateToken(token);
        if (!"refresh".equals(claims.get("type"))) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return claims;
    }
}
