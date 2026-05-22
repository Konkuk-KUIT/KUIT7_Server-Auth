package com.kuit.kuit4serverauth.repository;

import com.kuit.kuit4serverauth.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
    }

    public User findByRefreshToken(String refreshToken) {
        String sql = "SELECT * FROM users WHERE refresh_token = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), refreshToken);
    }

    public void updateRefreshToken(String username, String refreshToken) {
        String sql = "UPDATE users SET refresh_token = ? WHERE username = ?";
        jdbcTemplate.update(sql, refreshToken, username);
    }
}
