package com.example.flashsale.service;

import com.example.flashsale.entity.User;
import com.example.flashsale.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User register(String username, String password) {
        String hashed = passwordEncoder.encode(password);
        User user = User.builder().username(username).password(hashed).build();
        return userRepository.save(user);
    }

    public boolean checkPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }
}
