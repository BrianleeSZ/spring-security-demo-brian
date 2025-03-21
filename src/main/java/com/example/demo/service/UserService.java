package com.example.demo.service;

import com.example.demo.config.PasswordEncoderConfig;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String rawPassword) {
        // 加密密码
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        // 创建用户对象并保存到数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setRole(User.Role.USER); // 假设默认角色为 USER

        return userRepository.save(user);
    }
    public User registerUser(String username, String rawPassword,String type) {
        // 加密密码
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        // 创建用户对象并保存到数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setRole(User.Role.ADMIN); // 假设默认角色为 USER

        return userRepository.save(user);
    }
}