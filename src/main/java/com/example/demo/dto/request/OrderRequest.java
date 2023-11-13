package com.example.demo.dto.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
//    private String status;
    private List<OrderProductRequest> orderProduct;
    private String city;
    private String district;
    private String wards;
    private String addressInfo;
    private String phoneNumber;
}
