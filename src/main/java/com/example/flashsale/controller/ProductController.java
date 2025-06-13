package com.example.flashsale.controller;

import com.example.flashsale.entity.Product;
import com.example.flashsale.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品相關 API
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    @Operation(summary = "查詢所有商品")
    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查詢單一商品")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
