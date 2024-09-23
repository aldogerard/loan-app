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
import com.enigmacamp.loanapp.utils.CurrentUserUtil;
import com.enigmacamp.loanapp.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;
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
            CurrentUserUtil.setEmail(customer.getUser().getEmail());
            customerRepository.save(customer);
            CurrentUserUtil.clear();
        }catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, Message.IS_EXIST_CUSTOMER);
        }
    }

    @Override
    public List<CustomerResponse> getAllCustomer() {
        List<Customer> customerList = customerRepository.findAll();
        if (customerList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, Message.NOT_FOUND_CUSTOMER);
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
    public CustomerResponse updateCustomerById(CustomerRequest customerRequest) {
        validationUtil.validate(customerRequest);
        Customer findCustomer = getByIdOrThrow(customerRequest.getId());
        String findPath = null;
        ProfilePicture profilePicture = null;

        findCustomer.setFirstName(customerRequest.getFirstName());
        findCustomer.setLastName(customerRequest.getLastName());
        findCustomer.setPhone(customerRequest.getPhone());
        findCustomer.setStatus(customerRequest.getStatus());

        if(customerRequest.getMultipartFile() != null){
            if (getByIdOrThrow(customerRequest.getId()).getProfilePicture() != null){
                findPath = getByIdOrThrow(customerRequest.getId()).getProfilePicture().getPath();
            }
            profilePicture = profilePictureService.createProfilePicture(customerRequest.getMultipartFile());
            findCustomer.setProfilePicture(profilePicture);
        }

        Date dateOfBirth = customerRequest.getDateOfBirth() != null ? customerRequest.getDateOfBirth() : null;
        if (dateOfBirth != null) {
            findCustomer.setDateOfBirth(dateOfBirth);
        }

        customerRepository.saveAndFlush(findCustomer);

        if(customerRequest.getMultipartFile() != null && findPath != null) {
            profilePictureService.deleteProfilePictureByPath(findPath);
        }

        return CustomerMapper.customerToCustomerResponse(findCustomer);
    }

    @Override
    public CustomerResponse deleteCustomerById(String id) {
        Customer customer = getByIdOrThrow(id);
        customerRepository.deleteById(id);

        if(customer.getProfilePicture() != null){
            profilePictureService.deleteProfilePictureByPath(customer.getProfilePicture().getPath());
        }

        userService.deleteUserById(customer.getUser().getId());
        return CustomerMapper.customerToCustomerResponse(customer);
    }

    private Customer getByIdOrThrow(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Message.NOT_FOUND_CUSTOMER));
    }

}
