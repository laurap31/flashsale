package com.example.flashsale.controller;

import com.example.flashsale.entity.FlashOrder;
import com.example.flashsale.repository.FlashOrderRepository;
import com.example.flashsale.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class FlashOrderController {

    private final FlashOrderRepository orderRepository;
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "查詢我自己的所有搶購訂單")
    public ResponseEntity<List<FlashOrder>> myOrders(Authentication auth) {
        String username = auth.getName();
        Long userId = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("找不到用戶"))
                .getId();
        List<FlashOrder> orders = orderRepository.findAllByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}
