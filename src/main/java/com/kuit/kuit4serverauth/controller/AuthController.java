package com.kuit.kuit4serverauth.controller;

import com.kuit.kuit4serverauth.annotation.AuthUser;
import com.kuit.kuit4serverauth.exception.CustomException;
import com.kuit.kuit4serverauth.exception.ErrorCode;
import com.kuit.kuit4serverauth.model.LoginUser;
import com.kuit.kuit4serverauth.model.User;
import com.kuit.kuit4serverauth.repository.UserRepository;
import com.kuit.kuit4serverauth.service.JwtUtil;
import com.kuit.kuit4serverauth.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new CustomException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }

        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());
        refreshTokenService.save(user.getId(), refreshToken);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        return ResponseEntity.ok(response);
    }

    // Access Token 재발급 (Refresh Token Rotation 적용)
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.MISSING_AUTH_HEADER);
        }

        Claims claims = jwtUtil.validateToken(refreshToken);
        Long userId = jwtUtil.getUserId(claims);
        String username = claims.getSubject();

        if (!refreshTokenService.validate(userId, refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        User user = userRepository.findByUsername(username);
        String newAccessToken = jwtUtil.generateToken(username, user.getRole(), userId);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, username);
        refreshTokenService.save(userId, newRefreshToken); // 기존 토큰 교체 (Rotation)

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("refreshToken", newRefreshToken);
        return ResponseEntity.ok(response);
    }

    // 로그아웃: Refresh Token 즉시 무효화
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthUser LoginUser loginUser) {
        refreshTokenService.delete(loginUser.getUserId());
        return ResponseEntity.ok("Logged out successfully");
    }
}
