package com.enigmacamp.loanapp.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CustomerRequest {
    private String id;
    private String firstName;
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Pattern(
            regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])",
            message = "Date of Birth must be in the format YYYY-MM-DD, and must be a valid date"
    )
    private String dateOfBirth;

    @Size(min = 10, max = 15, message = "Nomor telepon harus memiliki panjang antara 10 dan 15 karakter")
    private String phone;

    private String status;

    @Email(message = "Email format must be correct")
    private String email;

    private MultipartFile multipartFile;
}
