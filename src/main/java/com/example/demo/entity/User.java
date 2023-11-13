package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.engine.spi.ManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    @NotBlank(message = "Invalid Username")
    private String username;

    @NotBlank(message = "Invalid password")
    @Min(value = 5, message = "Password more than 6 characters")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "FullName cannot be blank")
    private String fullName;

    @NotBlank(message = "PhoneNumber cannot be blank")
    private String phoneNumber;
    private String role;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> carts;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;


}
