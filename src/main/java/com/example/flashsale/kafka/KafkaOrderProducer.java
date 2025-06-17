package com.example.flashsale.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaOrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrder(String orderJson) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("flashsale-order", orderJson);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Send order failed: {}", orderJson, ex);
            } else {
                log.info("Send order success: {}", orderJson);
            }
        });
    }
}
