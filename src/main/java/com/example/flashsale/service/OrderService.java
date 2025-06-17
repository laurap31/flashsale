package com.example.flashsale.service;

import com.example.flashsale.kafka.KafkaOrderProducer;
import com.example.flashsale.repository.FlashOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final StringRedisTemplate redisTemplate;
    private final KafkaOrderProducer kafkaOrderProducer;
    private final FlashOrderRepository orderRepository;

    /**
     * 高併發下單：
     * 1. Redis 扣庫存，庫存充足才送入 Kafka
     * 2. Kafka 消費者異步寫入 DB
     */
    public boolean flashSale(Long productId, Long userId) {
        // 檢查是否已搶過
        if (orderRepository.existsByUserIdAndProductId(userId, productId)) {
            return false;
        }

        String stockKey = "flashsale:stock:" + productId;
        Long stock = redisTemplate.opsForValue().decrement(stockKey);

        if (stock == null || stock < 0) {
            return false; // 庫存不足
        }
        // 送入 Kafka
        String json = "{\"userId\":" + userId + ",\"productId\":" + productId + "}";
        kafkaOrderProducer.sendOrder(json);
        return true;
    }

    // 初始化商品庫存到 Redis
    public void initStockToRedis(Long productId, Integer stock) {
        redisTemplate.opsForValue().set("flashsale:stock:" + productId, String.valueOf(stock));
    }
}
