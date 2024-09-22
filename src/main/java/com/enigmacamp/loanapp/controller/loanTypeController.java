package com.enigmacamp.loanapp.controller;


import com.enigmacamp.loanapp.base.BaseResponse;
import com.enigmacamp.loanapp.constant.strings.Message;
import com.enigmacamp.loanapp.constant.strings.PathApi;
import com.enigmacamp.loanapp.dto.request.LoanTypeRequest;
import com.enigmacamp.loanapp.entity.LoanType;
import com.enigmacamp.loanapp.service.LoanTypeService;
import com.enigmacamp.loanapp.utils.RoleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        loanTypeRequest.setRole(RoleUtil.getName(auth));

        LoanType loanType = loanTypeService.createLoanType(loanTypeRequest);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_CREATE_LOAN_TYPE, HttpStatus.OK.value(), loanType);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllInstalmentType() {
        List<LoanType> loanTypeList = loanTypeService.getAllLoanTypes();
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_GET_ALL_LOAN_TYPE, HttpStatus.OK.value(), loanTypeList);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping(PathApi.BY_ID)
    public ResponseEntity<?> getInstalmentTypeById(@PathVariable String id) {
        LoanType instalmentType = loanTypeService.getLoanTypeById(id);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_GET_LOAN_TYPE, HttpStatus.OK.value(), instalmentType);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @PutMapping()
    public ResponseEntity<?> updateInstalmentType(@RequestBody LoanTypeRequest loanTypeRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        loanTypeRequest.setRole(RoleUtil.getName(auth));

        LoanType loanType = loanTypeService.updateLoanType(loanTypeRequest);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_UPDATE_LOAN_TYPE, HttpStatus.OK.value(), loanType);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @DeleteMapping(PathApi.BY_ID)
    public ResponseEntity<?> deleteInstalmentTypeById(@PathVariable String id) {
        LoanType loanType = loanTypeService.deleteLoanType(id);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_DELETE_INSTALMENT_TYPE, HttpStatus.OK.value(), loanType);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }
}
