package com.kuit.kuit4serverauth.service;

import com.kuit.kuit4serverauth.exception.CustomException;
import com.kuit.kuit4serverauth.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; // 추가됨!
import org.springframework.stereotype.Component;

import java.security.Key; // 추가됨!
import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "my-super-secret-key-for-kuit-auth-mission-must-be-long";
    private final long expirationMs = 3600000; // 1 hour

    // 🌟 핵심: 단순 문자열을 진짜 암호화 Key 객체로 바꿔주는 녀석
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                // 수정: secret 문자열 대신 Key 객체를 넣음
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            // 0.11.x 최신 버전에 맞는 파서 빌더 방식으로 수정
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Key 객체 사용
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }
}