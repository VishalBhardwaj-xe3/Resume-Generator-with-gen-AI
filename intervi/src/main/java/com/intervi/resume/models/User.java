package com.intervi.resume.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

import lombok.Data;

@Data
    @Document(collection = "users")
public class User {

    @Id
    private String Id;

    @NotBlank(message = "Usename is required")
    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @CreatedDate
    private LocalDateTime createdAt;

}
