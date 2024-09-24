package com.enigmacamp.loanapp.service;

import com.enigmacamp.loanapp.entity.LoanTransactionDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoanTransactionDetailService{
    void createLoanTransactionDetails(List<LoanTransactionDetail> loanTransactionDetails);
    void updateLoanTransactionDetails(List<LoanTransactionDetail> loanTransactionDetails);
    List<LoanTransactionDetail> getLoanTransactionDetail(String id);
}
