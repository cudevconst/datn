package com.example.demo.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeInfoForm {
    @Column(name = "username", unique = true)
    @NotBlank(message = "Invalid Username")
    private String username;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "FullName cannot be blank")
    private String fullName;

    @NotBlank(message = "PhoneNumber cannot be blank")
    private String phoneNumber;
}
