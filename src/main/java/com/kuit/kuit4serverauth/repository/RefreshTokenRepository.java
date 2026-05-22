package com.kuit.kuit4serverauth.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

// 보안적으로 RefreshToken 저장소는 있는게 좋다고해서 만들어봤어요...
@Repository
public class RefreshTokenRepository {

   private Map<String, String> refreshTokenMap = new HashMap<>();

    public void save(String username , String refreshToken){
        refreshTokenMap.put(username,refreshToken);
    }

    public String findByUsername(String username){
        return refreshTokenMap.get(username);
    }

}
