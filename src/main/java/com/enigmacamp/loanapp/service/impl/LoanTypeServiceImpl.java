package com.enigmacamp.loanapp.service.impl;

import com.enigmacamp.loanapp.constant.strings.Message;
import com.enigmacamp.loanapp.dto.request.LoanTypeRequest;
import com.enigmacamp.loanapp.entity.InstalmentType;
import com.enigmacamp.loanapp.entity.LoanType;
import com.enigmacamp.loanapp.repository.LoanTypeRepository;
import com.enigmacamp.loanapp.service.LoanTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanTypeServiceImpl implements LoanTypeService {

    private final LoanTypeRepository loanTypeRepository;

    @Override
    public LoanType createLoanType(LoanTypeRequest loanTypeRequest) {
        try {
            LoanType loanType = LoanType.builder()
                    .type(loanTypeRequest.getType())
                    .maxLoan(loanTypeRequest.getMaxLoan())
                    .build();
            loanTypeRepository.save(loanType);
            return loanType;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Message.BAD_REQUEST);
        }
    }

    @Override
    public LoanType getLoanTypeById(String id) {
        return getByIdOrThrow(id);
    }

    @Override
    public List<LoanType> getAllLoanTypes() {
        List<LoanType> loanTypes = loanTypeRepository.findAll();
        if (loanTypes.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return loanTypes;
    }

    @Override
    public LoanType updateLoanType(LoanTypeRequest loanTypeRequest) {
        try {
            LoanType findLoanTypes = getByIdOrThrow(loanTypeRequest.getId());
            if (findLoanTypes == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            findLoanTypes.setType(loanTypeRequest.getType());
            findLoanTypes.setMaxLoan(loanTypeRequest.getMaxLoan());

            loanTypeRepository.saveAndFlush(findLoanTypes);
            return findLoanTypes;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public LoanType deleteLoanType(String id) {
        LoanType findLoanTypes = getByIdOrThrow(id);
        if (findLoanTypes == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        loanTypeRepository.delete(findLoanTypes);
        return findLoanTypes;
    }

    private LoanType getByIdOrThrow(String id) {
        Optional<LoanType> loanType = loanTypeRepository.findById(id);
        return loanType.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Message.NOT_FOUND));
    }
}
