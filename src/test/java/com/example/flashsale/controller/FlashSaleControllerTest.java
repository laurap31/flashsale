package com.example.flashsale.controller;

import com.example.flashsale.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FlashSaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(username = "testuser")
    void flashSale_Success() throws Exception {
        when(orderService.flashSale(any(), any())).thenReturn(true);

        mockMvc.perform(post("/api/flashsale/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("搶購成功"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void flashSale_Fail() throws Exception {
        when(orderService.flashSale(any(), any())).thenReturn(false);

        mockMvc.perform(post("/api/flashsale/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("庫存不足/已搶過"));
    }
}
