package com.enigmacamp.loanapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LoanTransactionRequest {
    private String loanId;
    private String instalmentId;
    private String customerId;
    private Double nominal;
}
