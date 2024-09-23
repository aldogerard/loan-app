package com.enigmacamp.loanapp.controller;


import com.enigmacamp.loanapp.base.BaseResponse;
import com.enigmacamp.loanapp.constant.strings.Message;
import com.enigmacamp.loanapp.constant.strings.PathApi;
import com.enigmacamp.loanapp.dto.request.LoanTypeRequest;
import com.enigmacamp.loanapp.entity.LoanType;
import com.enigmacamp.loanapp.service.LoanTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.enigmacamp.loanapp.mapper.BaseMapper.mapToBaseResponse;

@RestController
@RequestMapping(PathApi.LOAN_TYPES)
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
public class loanTypeController {

    private final LoanTypeService loanTypeService;

    @PostMapping()
    public ResponseEntity<?> createInstalmentType(@RequestBody LoanTypeRequest loanTypeRequest) {

        LoanType loanType = loanTypeService.createLoanType(loanTypeRequest);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_CREATE, HttpStatus.OK.value(), loanType);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllInstalmentType() {
        List<LoanType> loanTypeList = loanTypeService.getAllLoanTypes();
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_GET_ALL, HttpStatus.OK.value(), loanTypeList);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping(PathApi.BY_ID)
    public ResponseEntity<?> getInstalmentTypeById(@PathVariable String id) {
        LoanType instalmentType = loanTypeService.getLoanTypeById(id);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_GET, HttpStatus.OK.value(), instalmentType);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @PutMapping()
    public ResponseEntity<?> updateInstalmentType(@RequestBody LoanTypeRequest loanTypeRequest) {
        LoanType loanType = loanTypeService.updateLoanType(loanTypeRequest);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_UPDATE, HttpStatus.OK.value(), loanType);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @DeleteMapping(PathApi.BY_ID)
    public ResponseEntity<?> deleteInstalmentTypeById(@PathVariable String id) {
        LoanType loanType = loanTypeService.deleteLoanType(id);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_DELETE, HttpStatus.OK.value(), loanType);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }
}
