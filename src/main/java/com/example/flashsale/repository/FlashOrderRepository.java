package com.example.flashsale.repository;

import com.example.flashsale.entity.FlashOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashOrderRepository extends JpaRepository<FlashOrder, Long> {
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
