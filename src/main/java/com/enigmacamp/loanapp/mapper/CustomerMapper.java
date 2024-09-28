package com.enigmacamp.loanapp.mapper;

import com.enigmacamp.loanapp.dto.request.CustomerRequest;
import com.enigmacamp.loanapp.dto.response.CustomerResponse;
import com.enigmacamp.loanapp.entity.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerMapper {
    public static Customer customerRequestToCustomer(CustomerRequest customerRequest)  {
        LocalDate date = null;
        try {
             date = LocalDate.parse(customerRequest.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception ignored) {

        }
        return Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .dateOfBirth(date)
                .phone(customerRequest.getPhone())
                .status(customerRequest.getStatus())
                .build();
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
                .createdBy(customer.getCreatedBy())
                .updatedAt(customer.getUpdatedAt())
                .updatedBy(customer.getUpdatedBy())
                .profilePicture(url)
                .build();
    }
}
