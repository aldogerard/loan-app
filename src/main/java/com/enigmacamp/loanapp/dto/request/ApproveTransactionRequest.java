package com.enigmacamp.loanapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ApproveTransactionRequest {
    private String adminId;
    private String loanTransactionId;
    private Integer interestRates;
}
