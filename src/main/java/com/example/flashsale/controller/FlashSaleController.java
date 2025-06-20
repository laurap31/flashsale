package com.example.flashsale.controller;

import com.example.flashsale.dto.OrderRequest;
import com.example.flashsale.entity.User;
import com.example.flashsale.repository.ProductRepository;
import com.example.flashsale.service.OrderService;
import com.example.flashsale.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flashsale")
@RequiredArgsConstructor
public class FlashSaleController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/order")
    @Operation(summary = "高併發搶購下單（需JWT）")
    public ResponseEntity<String> flashSale(
            @RequestBody OrderRequest req,
            Authentication auth // Spring Security 會自動注入目前登入用戶
    ) {
        // 從 JWT 解析 username，再查 userId
        String username = auth.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("找不到用戶"));
        Long userId = user.getId();

        boolean success = orderService.flashSale(req.getProductId(), userId);
        return success ? ResponseEntity.ok("搶購成功") : ResponseEntity.badRequest().body("庫存不足/已搶過");
    }

    // 初始化 Redis 庫存（只供管理員或開發用）
    @PostMapping("/init-stock")
    public ResponseEntity<String> initStock(@RequestParam Long productId, @RequestParam Integer stock) {
        orderService.initStockToRedis(productId, stock);
        return ResponseEntity.ok("庫存初始化完成");
    }
}
