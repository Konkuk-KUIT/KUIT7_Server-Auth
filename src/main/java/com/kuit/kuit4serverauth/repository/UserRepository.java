package com.kuit.kuit4serverauth.repository;

import com.kuit.kuit4serverauth.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        // 사용자가 없으면 예외 대신 빈 리스트를 반환
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username);
        // 리스트가 비어있으면 null을 반환하여 AuthController가 처리하도록 함
        return users.isEmpty() ? null : users.get(0);
    }
}
