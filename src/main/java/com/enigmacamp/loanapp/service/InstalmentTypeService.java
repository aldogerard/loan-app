package com.enigmacamp.loanapp.service;

import com.enigmacamp.loanapp.dto.request.InstalmentTypeRequest;
import com.enigmacamp.loanapp.entity.InstalmentType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InstalmentTypeService {
    InstalmentType createInstalmentType(InstalmentTypeRequest instalmentTypeRequest);
    InstalmentType getInstalmentTypeById(String id);
    List<InstalmentType> getAllInstalmentTypes();
    InstalmentType updateInstalmentType(InstalmentTypeRequest instalmentTypeRequest);
    void deleteInstalmentType(String id);
}
