package com.enigmacamp.loanapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LoanTransactionDetailResponse {
    private String transactionId;
    private Long transactionDate;
    private Double nominal;
    private String loanStatus;
    private Long createdAt;
    private Long updatedAt;
}
