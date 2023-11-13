package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Double amout;
    private String status;
    private LocalDateTime createAt;
    private String city;
    private String district;
    private String wards;
    private String addressInfo;
    private String phoneNumber;
    private List<OrderProductResponse> product;
}
