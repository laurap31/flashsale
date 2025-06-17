package com.example.flashsale.dto;

import lombok.Data;

/** 前端搶購下單時傳入的資料 */
@Data
public class OrderRequest {
    private Long productId;
    // userId 可從 token 解析，不必前端傳
}
