package com.example.flashsale.service;

import com.example.flashsale.kafka.KafkaOrderProducer;
import com.example.flashsale.repository.FlashOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private KafkaOrderProducer kafkaOrderProducer;
    @Mock
    private FlashOrderRepository orderRepository;
    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testFlashSale_Success() {
        Long productId = 1L;
        Long userId = 1L;

        when(orderRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(false);
        when(redisTemplate.opsForValue().decrement("flashsale:stock:" + productId)).thenReturn(10L);

        boolean result = orderService.flashSale(productId, userId);

        assertTrue(result);
        verify(kafkaOrderProducer, times(1)).sendOrder(any());
    }

    @Test
    void testFlashSale_StockNotEnough() {
        Long productId = 1L;
        Long userId = 2L;

        when(orderRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(false);
        when(redisTemplate.opsForValue().decrement("flashsale:stock:" + productId)).thenReturn(-1L);

        boolean result = orderService.flashSale(productId, userId);

        assertFalse(result);
        verify(kafkaOrderProducer, never()).sendOrder(any());
    }

    @Test
    void testFlashSale_AlreadyOrdered() {
        Long productId = 1L;
        Long userId = 3L;

        when(orderRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(true);

        boolean result = orderService.flashSale(productId, userId);

        assertFalse(result);
        verify(kafkaOrderProducer, never()).sendOrder(any());
    }
}
