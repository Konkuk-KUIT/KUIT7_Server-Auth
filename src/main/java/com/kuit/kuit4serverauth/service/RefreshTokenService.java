package com.kuit.kuit4serverauth.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

// Redis 대용 인메모리 저장소 (실제 운영에서는 Redis 사용)
// Key: userId, Value: refreshToken
@Service
public class RefreshTokenService {
    private final ConcurrentHashMap<Long, String> store = new ConcurrentHashMap<>();

    public void save(Long userId, String refreshToken) {
        store.put(userId, refreshToken);
    }

    public boolean validate(Long userId, String refreshToken) {
        String stored = store.get(userId);
        return stored != null && stored.equals(refreshToken);
    }

    public void delete(Long userId) {
        store.remove(userId);
    }
}
