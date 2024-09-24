package com.enigmacamp.loanapp.service.impl;

import com.enigmacamp.loanapp.constant.enums.EApprovalStatus;
import com.enigmacamp.loanapp.constant.enums.EInstalmentType;
import com.enigmacamp.loanapp.constant.enums.ELoanStatus;
import com.enigmacamp.loanapp.dto.request.ApproveTransactionRequest;
import com.enigmacamp.loanapp.dto.request.LoanTransactionRequest;
import com.enigmacamp.loanapp.dto.response.LoanTransactionDetailResponse;
import com.enigmacamp.loanapp.dto.response.LoanTransactionResponse;
import com.enigmacamp.loanapp.entity.*;
import com.enigmacamp.loanapp.repository.LoanTransactionRepository;
import com.enigmacamp.loanapp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanTransactionServiceImpl implements LoanTransactionService {

    private final LoanTransactionRepository loanTransactionRepository;
    private final LoanTransactionDetailService loanTransactionDetailService;
    private final InstalmentTypeService instalmentTypeService;
    private final LoanTypeService loanTypeService;
    private final CustomerService customerService;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoanTransactionResponse createLoanTransaction(LoanTransactionRequest loanTransactionRequest) {
        try {
            Customer customer = customerService.getById(loanTransactionRequest.getCustomerId());
            InstalmentType instalmentType = instalmentTypeService.getInstalmentTypeById(loanTransactionRequest.getInstalmentId());
            LoanType loanType = loanTypeService.getLoanTypeById(loanTransactionRequest.getLoanId());

            if (loanType.getMaxLoan() < loanTransactionRequest.getNominal()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan type is too large.");
            }

            LoanTransaction loanTransaction = LoanTransaction.builder()
                    .customer(customer)
                    .instalmentType(instalmentType)
                    .loanType(loanType)
                    .nominal(loanTransactionRequest.getNominal())
                    .build();

            loanTransactionRepository.saveAndFlush(loanTransaction);

            return LoanTransactionResponse.builder()
                    .id(loanTransaction.getId())
                    .loanTypeId(loanTransaction.getLoanType().getId())
                    .instalmentTypeId(loanTransaction.getInstalmentType().getId())
                    .customerId(loanTransaction.getCustomer().getId())
                    .nominal(loanTransaction.getNominal())
                    .createdAt(loanTransaction.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                    .build();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoanTransactionResponse approveLoanTransaction(ApproveTransactionRequest approveTransactionRequest) {
        try {
            AppUser user = userService.loadUserByUserId(approveTransactionRequest.getAdminId());
            LoanTransaction loanTransaction = getByIdOrThrow(approveTransactionRequest.getLoanTransactionId());

            Map<EInstalmentType, Integer> monthMap = Map.of(
                    EInstalmentType.ONE_MONTH, 1,
                    EInstalmentType.THREE_MONTHS, 2,
                    EInstalmentType.SIXTH_MONTHS, 3,
                    EInstalmentType.NINE_MONTHS, 9,
                    EInstalmentType.TWELVE_MONTHS, 12
            );
            Integer month = monthMap.get(loanTransaction.getInstalmentType().getName());

            loanTransaction.setApprovedBy(user.getEmail());
            loanTransaction.setApprovedAt(LocalDateTime.now());
            loanTransaction.setApprovalStatus(EApprovalStatus.APPROVED);

            loanTransactionRepository.saveAndFlush(loanTransaction);

            double totalInterestRate = ((double) loanTransaction.getNominal() * month / 100);
            double finalPayment = loanTransaction.getNominal() + totalInterestRate;

            List<LoanTransactionDetail> loanTransactionDetails = new ArrayList<>();
            for (int i = 0; i < month; i++) {
                double nominalPerMonth = (double) finalPayment / month;
                LoanTransactionDetail loanTransactionDetail = LoanTransactionDetail.builder()
                        .loanTransaction(loanTransaction)
                        .loanStatus(ELoanStatus.UNPAID)
                        .nominal(nominalPerMonth)
                        .build();
                loanTransactionDetails.add(loanTransactionDetail);
            }
            loanTransactionDetailService.createLoanTransactionDetails(loanTransactionDetails);

            loanTransaction.setLoanTransactionDetails(loanTransactionDetails);

            List<LoanTransactionDetailResponse> loanTransactionDetailResponse = loanTransaction.getLoanTransactionDetails().stream()
                    .map(loanTransactionDetail -> LoanTransactionDetailResponse.builder()
                            .transactionId(loanTransactionDetail.getId())
                            .nominal(loanTransactionDetail.getNominal())
                            .loanStatus(loanTransactionDetail.getLoanStatus().name())
                            .transactionDate(loanTransactionDetail.getTransactionDate() != null ? loanTransactionDetail.getTransactionDate().toInstant(ZoneOffset.UTC).toEpochMilli() : null)
                            .createdAt(loanTransactionDetail.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                            .updatedAt(loanTransactionDetail.getUpdatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                            .build())
                    .collect(Collectors.toList());


            return LoanTransactionResponse.builder()
                    .id(loanTransaction.getId())
                    .loanTypeId(loanTransaction.getLoanType().getId())
                    .instalmentTypeId(loanTransaction.getInstalmentType().getId())
                    .customerId(loanTransaction.getCustomer().getId())
                    .nominal(loanTransaction.getNominal())
                    .approvedAt(loanTransaction.getApprovedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                    .approvedBy(loanTransaction.getApprovedBy())
                    .approvalStatus(loanTransaction.getApprovalStatus().name())
                    .transactionDetail(loanTransactionDetailResponse)
                    .createdAt(loanTransaction.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                    .updatedAt(loanTransaction.getUpdatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public LoanTransactionResponse payLoanTransaction(String id) {
        try {
            LoanTransaction loanTransaction = getByIdOrThrow(id);
            List<LoanTransactionDetail> loanTransactionDetails = loanTransactionDetailService.getLoanTransactionDetail(id);
            AtomicInteger iterator = new AtomicInteger(1);
            loanTransactionDetails.stream()
                    .filter(loanTransactionDetail -> loanTransactionDetail.getLoanStatus().equals(ELoanStatus.UNPAID))
                    .filter(loanTransactionDetail -> iterator.getAndDecrement() > 0)
                    .forEach(loanTransactionDetail -> loanTransactionDetail.setLoanStatus(ELoanStatus.PAID));

            // UPDATE LOAN TRANSACTION DETAIL
            loanTransactionDetailService.updateLoanTransactionDetails(loanTransactionDetails);

            loanTransaction.setLoanTransactionDetails(loanTransactionDetails);
            loanTransactionRepository.saveAndFlush(loanTransaction);

            List<LoanTransactionDetailResponse> loanTransactionDetailResponse = loanTransaction.getLoanTransactionDetails().stream()
                    .map(loanTransactionDetail -> LoanTransactionDetailResponse.builder()
                            .transactionId(loanTransactionDetail.getId())
                            .nominal(loanTransactionDetail.getNominal())
                            .loanStatus(loanTransactionDetail.getLoanStatus().name())
                            .transactionDate(loanTransactionDetail.getTransactionDate() != null ? loanTransactionDetail.getTransactionDate().toInstant(ZoneOffset.UTC).toEpochMilli() : null)
                            .createdAt(loanTransactionDetail.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                            .updatedAt(loanTransactionDetail.getUpdatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                            .build())
                    .collect(Collectors.toList());

            return LoanTransactionResponse.builder()
                    .id(loanTransaction.getId())
                    .loanTypeId(loanTransaction.getLoanType().getId())
                    .instalmentTypeId(loanTransaction.getInstalmentType().getId())
                    .customerId(loanTransaction.getCustomer().getId())
                    .nominal(loanTransaction.getNominal())
                    .approvedAt(loanTransaction.getApprovedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                    .approvedBy(loanTransaction.getApprovedBy())
                    .approvalStatus(loanTransaction.getApprovalStatus().name())
                    .transactionDetail(loanTransactionDetailResponse)
                    .createdAt(loanTransaction.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                    .updatedAt(loanTransaction.getUpdatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                    .build();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public LoanTransactionResponse getLoanTransactionById(String id) {
        System.out.println(id);
        LoanTransaction loanTransaction = getByIdOrThrow(id);
        List<LoanTransactionDetailResponse> loanTransactionDetailResponse = null;
        if (loanTransaction != null) {
            loanTransactionDetailResponse = loanTransaction.getLoanTransactionDetails().stream().map(loanTransactionDetail ->
                    LoanTransactionDetailResponse.builder()
                            .transactionId(loanTransactionDetail.getId())
                            .transactionDate(loanTransactionDetail.getTransactionDate() != null ? loanTransactionDetail.getTransactionDate().toInstant(ZoneOffset.UTC).toEpochMilli() : null)
                            .nominal(loanTransactionDetail.getNominal())
                            .loanStatus(loanTransactionDetail.getLoanStatus().toString())
                            .createdAt(loanTransactionDetail.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                            .updatedAt(loanTransactionDetail.getUpdatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                            .build()
            ).collect(Collectors.toUnmodifiableList());
        }

        LoanTransactionResponse loanTransactionResponse = LoanTransactionResponse.builder()
               .id(loanTransaction.getId())
               .loanTypeId(loanTransaction.getLoanType().getId())
               .instalmentTypeId(loanTransaction.getInstalmentType().getId())
               .customerId(loanTransaction.getCustomer().getId())
               .nominal(loanTransaction.getNominal())
               .transactionDetail(loanTransactionDetailResponse)
               .createdAt(loanTransaction.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
               .updatedAt(loanTransaction.getUpdatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
               .build();
        if (loanTransaction.getApprovalStatus() != null) {
            loanTransactionResponse.setApprovalStatus(loanTransaction.getApprovalStatus().toString());
            loanTransactionResponse.setApprovedBy(loanTransaction.getApprovedBy());
            loanTransactionResponse.setApprovedAt(loanTransaction.getApprovedAt().toInstant(ZoneOffset.UTC).toEpochMilli());
        }
        return loanTransactionResponse;
    }

    private LoanTransaction getByIdOrThrow(String id) {
        Optional<LoanTransaction> loanTransaction = loanTransactionRepository.findById(id);
        return loanTransaction.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan transaction not found"));
    }

}
