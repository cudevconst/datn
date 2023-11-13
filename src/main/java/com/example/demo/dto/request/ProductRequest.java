package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private Double price;
    private String image1;
    private String image2;
    private String image3;
    private Set<String> sizes;
    private Set<String> colors;
    private List<Long> types;
}
