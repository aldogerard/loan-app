package com.enigmacamp.loanapp.service.impl;

import com.enigmacamp.loanapp.constant.enums.ELoanStatus;
import com.enigmacamp.loanapp.entity.LoanTransactionDetail;
import com.enigmacamp.loanapp.repository.LoanTransactionDetailRepository;
import com.enigmacamp.loanapp.service.LoanTransactionDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanTransactionDetailServiceImpl implements LoanTransactionDetailService {

    private final LoanTransactionDetailRepository loanTransactionDetailRepository;

    @Override
    public void createLoanTransactionDetails(List<LoanTransactionDetail> loanTransactionDetails) {
        loanTransactionDetailRepository.saveAllAndFlush(loanTransactionDetails);
    }

    @Override
    public void updateLoanTransactionDetails(List<LoanTransactionDetail> loanTransactionDetails) {
        for (LoanTransactionDetail loanTransactionDetail : loanTransactionDetails) {
            LoanTransactionDetail findLoanTransactionDetail = getById(loanTransactionDetail.getId());
            findLoanTransactionDetail.setTransactionDate(loanTransactionDetail.getTransactionDate());
            findLoanTransactionDetail.setLoanStatus(loanTransactionDetail.getLoanStatus());

            loanTransactionDetailRepository.saveAndFlush(findLoanTransactionDetail);
        }
    }

    @Override
    public List<LoanTransactionDetail> getLoanTransactionDetail(String id) {
        try {
            return loanTransactionDetailRepository.getLoanTransactionDetailByLoanTransactionId(id);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private LoanTransactionDetail getById(String id) {
        return loanTransactionDetailRepository.findById(id).orElseThrow();
    }
}
