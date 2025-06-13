package com.example.flashsale.controller;

import com.example.flashsale.security.JwtTokenUtil;
import com.example.flashsale.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    @Operation(summary = "註冊新用戶")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("用戶名已存在");
        }
        userService.register(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("註冊成功");
    }

    @PostMapping("/login")
    @Operation(summary = "用戶登入")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        var userOpt = userService.findByUsername(request.getUsername());
        if (userOpt.isEmpty() || !userService.checkPassword(request.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號或密碼錯誤");
        }
        String token = jwtTokenUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}

@Data
class AuthRequest {
    private String username;
    private String password;
}
