package com.enigmacamp.loanapp.controller;

import com.enigmacamp.loanapp.base.BaseResponse;
import com.enigmacamp.loanapp.constant.strings.Message;
import com.enigmacamp.loanapp.constant.strings.PathApi;
import com.enigmacamp.loanapp.dto.request.CustomerRequest;
import com.enigmacamp.loanapp.dto.response.CustomerResponse;
import com.enigmacamp.loanapp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

import static com.enigmacamp.loanapp.mapper.BaseMapper.mapToBaseResponse;

@RestController
@RequestMapping(PathApi.CUSTOMER)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<?> getAllCustomers() {
        List<CustomerResponse> customerResponseList = customerService.getAllCustomer();
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_GET_ALL_CUSTOMER, HttpStatus.OK.value(), customerResponseList);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping(PathApi.BY_ID)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_CUSTOMER')")
    public ResponseEntity<?> getCustomersById(@PathVariable  String id) {
        CustomerResponse customerResponse = customerService.getCustomerById(id);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_GET_ALL_CUSTOMER, HttpStatus.OK.value(), customerResponse);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_CUSTOMER')")
    public ResponseEntity<?> updateCustomerById(
            @RequestParam String id,
            @RequestParam(defaultValue = "", required = false) String firstName,
            @RequestParam(defaultValue = "", required = false) String lastName,
            @RequestParam(defaultValue = "", required = false) String email,
            @RequestParam(required = false) Date dateOfBirth,
            @RequestParam(defaultValue = "", required = false) String phone,
            @RequestParam(defaultValue = "", required = false) String status,
            @RequestParam(required = false) MultipartFile image
    ) {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .dateOfBirth(dateOfBirth)
                .phone(phone)
                .status(status)
                .multipartFile(image)
                .build();

        CustomerResponse customerResponse = customerService.updateCustomerById(customerRequest);
        BaseResponse<?> baseResponse = mapToBaseResponse(Message.SUCCESS_UPDATE_CUSTOMER, HttpStatus.OK.value(), customerResponse);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_CUSTOMER')")
    @DeleteMapping(PathApi.BY_ID)
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
        CustomerResponse customerResponse =  this.customerService.deleteCustomerById(id);
        BaseResponse<?> response = mapToBaseResponse("Successfully deleted customer", HttpStatus.OK.value(), customerResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
