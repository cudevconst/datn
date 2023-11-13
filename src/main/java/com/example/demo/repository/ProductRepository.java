package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlug(String slug);
    List<Product> findByIdIn(List<Long> ids);
    @Query(value = "SELECT * FROM product\n" +
            "INNER JOIN type_product\n" +
            "ON product.id = type_product.product_id\n" +
            "WHERE type_id = ?1", nativeQuery = true)
    List<Product> findByTypeId(Long typeId);
}
