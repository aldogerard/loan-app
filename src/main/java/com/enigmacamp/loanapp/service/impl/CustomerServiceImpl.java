package com.enigmacamp.loanapp.service.impl;

import com.enigmacamp.loanapp.constant.strings.Message;
import com.enigmacamp.loanapp.dto.request.CustomerRequest;
import com.enigmacamp.loanapp.dto.response.CustomerResponse;
import com.enigmacamp.loanapp.entity.Customer;
import com.enigmacamp.loanapp.entity.ProfilePicture;
import com.enigmacamp.loanapp.mapper.CustomerMapper;
import com.enigmacamp.loanapp.repository.CustomerRepository;
import com.enigmacamp.loanapp.service.CustomerService;
import com.enigmacamp.loanapp.service.ProfilePictureService;
import com.enigmacamp.loanapp.service.UserService;
import com.enigmacamp.loanapp.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;
    private final ProfilePictureService profilePictureService;
    private final UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCustomer(Customer customer) {
        try {
            customerRepository.saveAndFlush(customer);
        }catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,Message.IS_EXIST);
        }
    }

    @Override
    public List<CustomerResponse> getAllCustomer() {
        List<Customer> customerList = customerRepository.findAll();
        if (customerList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, Message.NOT_FOUND);
        return customerList.stream().map(CustomerMapper::customerToCustomerResponse).toList();
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        Customer customer = getByIdOrThrow(id);
        return CustomerMapper.customerToCustomerResponse(customer);
    }

    @Override
    public Customer getById(String id) {
        return getByIdOrThrow(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse updateCustomerById(CustomerRequest customerRequest) {
        try {
            validationUtil.validate(customerRequest);

            Customer findCustomer = getByIdOrThrow(customerRequest.getId());
            findCustomer.setFirstName(customerRequest.getFirstName());
            findCustomer.setLastName(customerRequest.getLastName());
            findCustomer.setPhone(customerRequest.getPhone());
            findCustomer.setStatus(customerRequest.getStatus());

            if (customerRequest.getDateOfBirth() != null) {
                LocalDate date = LocalDate.parse(customerRequest.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                findCustomer.setDateOfBirth(date);
            }
            String findPath = null;
            if(customerRequest.getMultipartFile() != null){
                if (findCustomer.getProfilePicture() != null){
                    findPath = findCustomer.getProfilePicture().getPath();
                }
                ProfilePicture profilePicture = profilePictureService.createProfilePicture(customerRequest.getMultipartFile());
                findCustomer.setProfilePicture(profilePicture);
            }

            customerRepository.saveAndFlush(findCustomer);

            if(customerRequest.getMultipartFile() != null && findPath != null){
                profilePictureService.deleteProfilePictureByPath(findPath);
            }

            return CustomerMapper.customerToCustomerResponse(findCustomer);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse deleteCustomerById(String id) {
        try {
            Customer customer = getByIdOrThrow(id);
            customerRepository.deleteById(id);

            if(customer.getProfilePicture() != null){
                profilePictureService.deleteProfilePictureByPath(customer.getProfilePicture().getPath());
            }

            userService.deleteUserById(customer.getUser().getId());
            return CustomerMapper.customerToCustomerResponse(customer);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private Customer getByIdOrThrow(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Message.NOT_FOUND));
    }

}
