package com.enigmacamp.loanapp.controller;

import com.enigmacamp.loanapp.base.BaseResponse;
import com.enigmacamp.loanapp.constant.strings.Message;
import com.enigmacamp.loanapp.constant.strings.PathApi;
import com.enigmacamp.loanapp.dto.request.LoanTransactionRequest;
import com.enigmacamp.loanapp.dto.response.LoanTransactionResponse;
import com.enigmacamp.loanapp.service.LoanTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.enigmacamp.loanapp.mapper.BaseMapper.mapToBaseResponse;

@RestController
@RequestMapping(PathApi.TRANSACTIONS)
@RequiredArgsConstructor
public class LoanTransactionController {

    private final LoanTransactionService loanTransactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<?> createLoanTransaction(@RequestBody LoanTransactionRequest loanTransactionRequest) {
        LoanTransactionResponse loanTransactionResponse = loanTransactionService.createLoanTransaction(loanTransactionRequest);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_CREATE_LOAN_TRANSACTION, HttpStatus.CREATED.value(), loanTransactionResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @GetMapping(PathApi.BY_ID)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<?> getLoanTransactionById(@PathVariable String id) {
        LoanTransactionResponse loanTransactionResponse = loanTransactionService.getLoanTransactionById(id);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_GET_LOAN_TRANSACTION, HttpStatus.OK.value(), loanTransactionResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

}
