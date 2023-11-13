package com.example.demo.dto.response;

import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TypeResponse {
    private Long id;

    private String nameType;
}
