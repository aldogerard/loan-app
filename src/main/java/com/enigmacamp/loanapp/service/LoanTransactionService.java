package com.enigmacamp.loanapp.service;

import com.enigmacamp.loanapp.dto.request.ApproveTransactionRequest;
import com.enigmacamp.loanapp.dto.request.LoanTransactionRequest;
import com.enigmacamp.loanapp.dto.response.LoanTransactionResponse;
import org.springframework.stereotype.Service;

@Service
public interface LoanTransactionService {
    LoanTransactionResponse createLoanTransaction(LoanTransactionRequest loanTransactionRequest);
    LoanTransactionResponse approveLoanTransaction(ApproveTransactionRequest approveTransactionRequest);
    LoanTransactionResponse getLoanTransactionById(String id);
}
