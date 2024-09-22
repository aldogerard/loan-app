package com.enigmacamp.loanapp.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LoanTypeRequest {
    private String id;
    private String type;
    @Positive
    private Double maxLoan;

    private String role;
}
