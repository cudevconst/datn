package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductResponse {
    private Long id;
    private ProductResponse product;
    private Integer quantity;
    private String color;
    private String size;
}
