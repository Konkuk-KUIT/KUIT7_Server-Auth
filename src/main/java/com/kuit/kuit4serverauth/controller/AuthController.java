package com.kuit.kuit4serverauth.controller;

import com.kuit.kuit4serverauth.exception.CustomException;
import com.kuit.kuit4serverauth.exception.ErrorCode;
import com.kuit.kuit4serverauth.model.User;
import com.kuit.kuit4serverauth.repository.UserRepository;
import com.kuit.kuit4serverauth.service.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.kuit.kuit4serverauth.exception.ErrorCode.MISSING_AUTH_HEADER;

@RestController
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final Map<String, String> refreshTokenStorage = new HashMap<>();

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new CustomException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        refreshTokenStorage.put(user.getUsername(), refreshToken);

        Map<String, String> response = new HashMap<>();
        response.put("Access-Token", accessToken);
        response.put("Refresh-Token", refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException(MISSING_AUTH_HEADER);
        }

        String refreshToken = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.validateToken(refreshToken);
            String username = claims.getSubject();

            String token = refreshTokenStorage.get(username);

            if(token == null || !token.equals(refreshToken)) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

            User user = userRepository.findByUsername(username);
            String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole());

            Map<String, String> response = new HashMap<>();
            response.put("Access-Token", accessToken);

            return ResponseEntity.ok(response);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
    }
}

