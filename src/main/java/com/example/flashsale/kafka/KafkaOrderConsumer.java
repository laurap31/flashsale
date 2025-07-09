package com.example.flashsale.kafka;

import com.example.flashsale.entity.FlashOrder;
import com.example.flashsale.repository.FlashOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderConsumer {

    private final FlashOrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    // 暫存訂單 queue
    private final Queue<FlashOrder> orderBuffer = new ConcurrentLinkedQueue<>();

    @KafkaListener(topics = "flashsale-order", groupId = "flashsale")
    public void consumeOrder(ConsumerRecord<String, String> record) throws Exception {
        // Step 1: 反序列化 JSON
        OrderMessage msg = objectMapper.readValue(record.value(), OrderMessage.class);

        // Step 2: 重複搶購校驗
        boolean exists = orderRepository.existsByUserIdAndProductId(msg.getUserId(), msg.getProductId());
        if (exists) {
            // 已經下單過，不重複入庫
            return;
        }

        // Step 3: 建立訂單（入庫 MySQL）
        FlashOrder order = FlashOrder.builder()
                .userId(msg.getUserId())
                .productId(msg.getProductId())
                .createTime(LocalDateTime.now())
                .build();

        orderBuffer.add(order);
    }

    @Scheduled(fixedRate = 100)
    public void batchInsert() {
        if (orderBuffer.isEmpty()) return;

        List<FlashOrder> batchList = new ArrayList<>();
        while (!orderBuffer.isEmpty() && batchList.size() < 100) {
            batchList.add(orderBuffer.poll());
        }

        if (!batchList.isEmpty()) {
            orderRepository.saveAll(batchList);
            log.info("批次入庫，準備寫入筆數：{}", batchList.size());
        }
    }

    // DTO for Kafka Message
    @Data
    public static class OrderMessage {
        private Long userId;
        private Long productId;
    }
}
