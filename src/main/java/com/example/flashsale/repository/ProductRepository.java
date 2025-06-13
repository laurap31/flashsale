package com.example.flashsale.repository;

import com.example.flashsale.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 商品 Repository，提供查詢、儲存商品
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 若有複雜查詢可在這裡擴充
}
