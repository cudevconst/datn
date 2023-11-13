package com.example.demo.repository;

import com.example.demo.entity.Invoice;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = "Select * from invoice i where i.order_id in (?1)", nativeQuery = true)
    List<Invoice> findByOrderIdIn(List<Long> orderIds);

}
