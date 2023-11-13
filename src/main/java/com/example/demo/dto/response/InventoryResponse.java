package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {
    private Long id;

    private String size;
    private String color;
    private Integer quantity;

    private Date dateInput;
    private ProductResponse productResponse;
}
