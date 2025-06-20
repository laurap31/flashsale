package com.example.flashsale.kafka;

import com.example.flashsale.entity.FlashOrder;
import com.example.flashsale.repository.FlashOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

class KafkaOrderConsumerTest {

    @Mock
    private FlashOrderRepository orderRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaOrderConsumer consumer;

    @Test
    void consumeOrder_insertOrderIfNotExists() throws Exception {
        MockitoAnnotations.openMocks(this);
        KafkaOrderConsumer.OrderMessage msg = new KafkaOrderConsumer.OrderMessage();
        msg.setUserId(1L);
        msg.setProductId(1L);

        String json = "{\"userId\":1,\"productId\":1}";
        when(objectMapper.readValue(json, KafkaOrderConsumer.OrderMessage.class)).thenReturn(msg);
        when(orderRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);

        // 模擬 Kafka 消息
        consumer.consumeOrder(new org.apache.kafka.clients.consumer.ConsumerRecord<>("flashsale-order", 0, 0L, null, json));

        verify(orderRepository, times(1)).save(any(FlashOrder.class));
    }
}
