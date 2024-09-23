package com.enigmacamp.loanapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LoanTransactionResponse {
    private String id;
    private String loanTypeId;
    private String instalmentTypeId;
    private String customerId;
    private double nominal;
    private Long approvedAt;
    private String approvedBy;
    private String approvalStatus;
    private List<LoanTransactionDetailResponse> transactionDetail;
    private long createdAt;
    private long updatedAt;
}
