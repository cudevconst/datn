package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRequest {

    private Long productId;
    private String size;
    private String color;
    private Integer quantity;
    private Date dateInput;
}
