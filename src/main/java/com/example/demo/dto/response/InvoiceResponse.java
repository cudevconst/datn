package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceResponse {
    private Long id;
    private Double amout;
    private Double transaction;
    private Double coupon;
    private String status;
    private String type;
    private LocalDateTime createAt;
    private OrderResponse order;
}
