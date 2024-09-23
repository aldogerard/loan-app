package com.enigmacamp.loanapp.controller;

import com.enigmacamp.loanapp.base.BaseResponse;
import com.enigmacamp.loanapp.constant.strings.Message;
import com.enigmacamp.loanapp.constant.strings.PathApi;
import com.enigmacamp.loanapp.dto.request.InstalmentTypeRequest;
import com.enigmacamp.loanapp.entity.InstalmentType;
import com.enigmacamp.loanapp.service.InstalmentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static com.enigmacamp.loanapp.mapper.BaseMapper.mapToBaseResponse;

@RestController
@RequestMapping(PathApi.INSTALMENT_TYPE)
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
public class InstalmentTypeController {

    private final InstalmentTypeService instalmentTypeService;

    @PostMapping()
    public ResponseEntity<?> createInstalmentType(@RequestBody InstalmentTypeRequest instalmentTypeRequest) {

        InstalmentType instalmentType = instalmentTypeService.createInstalmentType(instalmentTypeRequest);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_CREATE, HttpStatus.OK.value(), instalmentType);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllInstalmentType() {
        List<InstalmentType> instalmentTypeList = instalmentTypeService.getAllInstalmentTypes();
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_GET_ALL, HttpStatus.OK.value(), instalmentTypeList);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping(PathApi.BY_ID)
    public ResponseEntity<?> getInstalmentTypeById(@PathVariable String id) {
        InstalmentType instalmentType = instalmentTypeService.getInstalmentTypeById(id);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_GET, HttpStatus.OK.value(), instalmentType);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @PutMapping()
    public ResponseEntity<?> updateInstalmentType(@RequestBody InstalmentTypeRequest instalmentTypeRequest) {

        InstalmentType instalmentType = instalmentTypeService.updateInstalmentType(instalmentTypeRequest);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_UPDATE, HttpStatus.OK.value(), instalmentType);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @DeleteMapping(PathApi.BY_ID)
    public ResponseEntity<?> deleteInstalmentTypeById(@PathVariable String id) {
        InstalmentType instalmentType = instalmentTypeService.deleteInstalmentType(id);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_DELETE, HttpStatus.OK.value(), instalmentType);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }
}
