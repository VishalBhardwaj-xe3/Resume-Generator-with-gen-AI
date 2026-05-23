package com.intervi.resume.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

import lombok.Data;

@Data
    @Document(collection = "users")
public class User {

    @NotBlank(message = "Usename is required")
    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "password is required")
    private String password;

}
