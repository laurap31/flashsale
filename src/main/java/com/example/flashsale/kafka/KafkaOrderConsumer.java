package com.example.flashsale.kafka;

import com.example.flashsale.entity.FlashOrder;
import com.example.flashsale.repository.FlashOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class KafkaOrderConsumer {

    private final FlashOrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "flashsale-order", groupId = "flashsale")
    public void consumeOrder(ConsumerRecord<String, String> record) throws Exception {
        // 反序列化 JSON
        OrderMessage msg = objectMapper.readValue(record.value(), OrderMessage.class);
        // 新增訂單（這裡可以再檢查一次庫存）
        FlashOrder order = FlashOrder.builder()
                .userId(msg.getUserId())
                .productId(msg.getProductId())
                .createTime(LocalDateTime.now())
                .build();
        orderRepository.save(order);
        // 併發扣庫存這裡可做二次檢查
    }

    // DTO for message
    @Data
    public static class OrderMessage {
        private Long userId;
        private Long productId;
    }
}
