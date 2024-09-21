package com.enigmacamp.loanapp.mapper;

import com.enigmacamp.loanapp.dto.request.CustomerRequest;
import com.enigmacamp.loanapp.dto.response.CustomerResponse;
import com.enigmacamp.loanapp.entity.Customer;

import java.time.LocalDateTime;

public class CustomerMapper {
    public static Customer customerRequestToCustomer(CustomerRequest customerRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .dateOfBirth(customerRequest.getDateOfBirth())
                .phone(customerRequest.getPhone())
                .status(customerRequest.getStatus())
                .build();
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy(customerRequest.getEmail());
        return customer;
    }

    public static CustomerResponse customerToCustomerResponse(Customer customer) {
        String url = customer.getProfilePicture() == null ? null : customer.getProfilePicture().getUrl();

        return CustomerResponse.builder()
                .customerId(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(customer.getDateOfBirth())
                .phone(customer.getPhone())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .updatedBy(customer.getUpdatedBy())
                .profilePicture(url)
                .build();
    }
}
