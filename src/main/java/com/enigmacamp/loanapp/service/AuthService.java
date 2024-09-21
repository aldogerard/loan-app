package com.enigmacamp.loanapp.service;

import com.enigmacamp.loanapp.dto.request.AuthRequest;
import com.enigmacamp.loanapp.dto.response.LoginResponse;
import com.enigmacamp.loanapp.dto.response.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    RegisterResponse register(AuthRequest authRequest);
    RegisterResponse registerCustomer(AuthRequest authRequest);
    LoginResponse login(AuthRequest authRequest);
}
