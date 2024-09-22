package com.enigmacamp.loanapp.service;

import com.enigmacamp.loanapp.dto.request.LoanTypeRequest;
import com.enigmacamp.loanapp.entity.LoanType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoanTypeService {
    LoanType createLoanType(LoanTypeRequest loanType);
    LoanType getLoanTypeById(String id);
    List<LoanType> getAllLoanTypes();
    LoanType updateLoanType(LoanTypeRequest loanType);
    LoanType deleteLoanType(String id);
}
