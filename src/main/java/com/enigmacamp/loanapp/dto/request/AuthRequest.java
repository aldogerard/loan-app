package com.enigmacamp.loanapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AuthRequest {
    @NotBlank(message = "email is required")
    @Email(message = "Email format must be correct")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8, max = 15 , message = "Password must be between 8 and 15 characters")
    private String password;
}
