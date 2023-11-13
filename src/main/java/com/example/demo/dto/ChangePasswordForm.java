package com.example.demo.dto;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordForm {
    @NotBlank(message = "Cannot be blank")
    private String currentPassword;

    @NotBlank
    @Size(min = 5, message = "Password must be at least 6 characters long")
    private String newPassword;

    @NotBlank(message = "Cannot be blank")
    private String confirmPassword;
}
