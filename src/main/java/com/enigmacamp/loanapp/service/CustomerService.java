package com.enigmacamp.loanapp.service;

import com.enigmacamp.loanapp.dto.request.CustomerRequest;
import com.enigmacamp.loanapp.dto.response.CustomerResponse;
import com.enigmacamp.loanapp.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    void createCustomer(Customer customer);
    List<CustomerResponse> getAllCustomer();
    CustomerResponse getCustomerById(String id);
    Customer getById(String id);
    CustomerResponse updateCustomerById(CustomerRequest customerRequest);
    CustomerResponse deleteCustomerById(String id);
}
