package com.campushire.service;

import com.campushire.entity.User;
import com.campushire.repository.UserRepository;
import com.campushire.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public User registerUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Map<String, Object> login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPasswordHash().equals(password)) {
            String token = jwtUtil.generateToken(email, user.getRole());
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("role", user.getRole());
            result.put("userId", user.getUserId());
            result.put("email", user.getEmail());
            return result;
        }
        return null;
    }
}
