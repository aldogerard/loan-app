package com.enigmacamp.loanapp.service.impl;

import com.enigmacamp.loanapp.constant.enums.EInstalmentType;
import com.enigmacamp.loanapp.dto.request.InstalmentTypeRequest;
import com.enigmacamp.loanapp.entity.InstalmentType;
import com.enigmacamp.loanapp.repository.InstalmentTypeRepository;
import com.enigmacamp.loanapp.service.InstalmentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstalmentTypeServiceImpl implements InstalmentTypeService {

    private final InstalmentTypeRepository instalmentTypeRepository;

    @Override
    public InstalmentType createInstalmentType(InstalmentTypeRequest instalmentTypeRequest) {
        try {
            InstalmentType instalmentType = InstalmentType.builder()
                    .name(Enum.valueOf(EInstalmentType.class, instalmentTypeRequest.getInstalmentType()))
                    .createdAt(LocalDateTime.now())
                    .build();
            instalmentTypeRepository.save(instalmentType);
            return instalmentType;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "InstalmentType is exist");
        }
    }

    @Override
    public InstalmentType getInstalmentTypeById(String id) {
        return getByIdOrThrow(id);
    }

    @Override
    public List<InstalmentType> getAllInstalmentTypes() {
        List<InstalmentType> instalmentTypes = instalmentTypeRepository.findAll();
        if (instalmentTypes.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return instalmentTypes;
    }

    @Override
    public InstalmentType updateInstalmentType(InstalmentTypeRequest instalmentTypeRequest) {
        try {
            InstalmentType findInstalmentType = getByIdOrThrow(instalmentTypeRequest.getId());
            if (findInstalmentType == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            findInstalmentType.setName(Enum.valueOf(EInstalmentType.class, instalmentTypeRequest.getInstalmentType()));
            findInstalmentType.setUpdatedAt(LocalDateTime.now());

            instalmentTypeRepository.save(findInstalmentType);

            return findInstalmentType;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "InstalmentType is exist");
        }
    }

    @Override
    public void deleteInstalmentType(String id) {
        InstalmentType findInstalmentType = getByIdOrThrow(id);
        if (findInstalmentType == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        instalmentTypeRepository.delete(findInstalmentType);
    }

    private InstalmentType getByIdOrThrow(String id) {
        Optional<InstalmentType> instalmentType = instalmentTypeRepository.findById(id);
        return instalmentType.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instalment type not found"));
    }
}
