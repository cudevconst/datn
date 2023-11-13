package com.example.demo.repository;

import com.example.demo.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query(value = "SELECT sum(i.quantity) as quantity FROM inventory i where i.product_id = ?1 and i.color = ?2 and i.size = ?3", nativeQuery = true)
    Integer countQuantityProductByColorAndSize(Long productId, String color, String size);
}
