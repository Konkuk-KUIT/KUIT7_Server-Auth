package com.kuit.kuit4serverauth.service;

import com.kuit.kuit4serverauth.exception.CustomException;
import com.kuit.kuit4serverauth.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // 256비트(32바이트) 이상의 안전한 비밀키
    private final String secret = "kuit-4th-server-auth-study-mission-for-8th-week-secret-key";
    private final long accessTokenExpirationMs = 3600000;   // 1 hour
    private final long refreshTokenExpiration = 1209600000; // 14일 (1000 * 60 * 60 * 24 * 14)

    // 문자열 비밀키를 Key 객체로 변환하는 메서드
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 기존 generateToken 메서드를 Access Token 발급 메서드로 수정
    public String generateAccessToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                // 단순 문자열 대신 Key 객체를 사용하여 서명
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            // 최신 라이브러리 방식인 parserBuilder()와 Key 객체 사용
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {   // 만료된 토큰
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {             // 위변조된 토큰
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }
}