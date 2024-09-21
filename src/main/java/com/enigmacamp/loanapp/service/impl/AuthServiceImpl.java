package com.enigmacamp.loanapp.service.impl;

import com.enigmacamp.loanapp.constant.enums.ERole;
import com.enigmacamp.loanapp.dto.request.AuthRequest;
import com.enigmacamp.loanapp.dto.response.LoginResponse;
import com.enigmacamp.loanapp.dto.response.RegisterResponse;
import com.enigmacamp.loanapp.entity.*;
import com.enigmacamp.loanapp.repository.UserRepository;
import com.enigmacamp.loanapp.security.JwtUtil;
import com.enigmacamp.loanapp.service.AuthService;
import com.enigmacamp.loanapp.service.CustomerService;
import com.enigmacamp.loanapp.service.RoleService;
import com.enigmacamp.loanapp.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final RoleService roleService;
    private final ValidationUtil validationUtil;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerCustomer(AuthRequest authRequest) {
        try {
            this.validationUtil.validate(authRequest);

            List<Role> roles = List.of(this.roleService.getOrCreate(Role.builder().name(ERole.ROLE_CUSTOMER).build()));

            User user = User.builder()
                    .email(authRequest.getEmail().toLowerCase())
                    .password(this.passwordEncoder.encode(authRequest.getPassword()))
                    .roles(roles)
                    .build();

            this.userRepository.saveAndFlush(user);

            Customer customer = Customer.builder()
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .build();

            this.customerService.createCustomer(customer);
            return this.getRegisterResponse(user);
        }catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer already exists");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse register(AuthRequest authRequest) {
        try {
            this.validationUtil.validate(authRequest);

            List<Role> roles = List.of(
                    this.roleService.getOrCreate(Role.builder().name(ERole.ROLE_ADMIN).build()),
                    this.roleService.getOrCreate(Role.builder().name(ERole.ROLE_STAFF).build())
            );

            User user = User.builder()
                .email(authRequest.getEmail().toLowerCase())
                .password(this.passwordEncoder.encode(authRequest.getPassword()))
                .roles(roles)
                .build();

            this.userRepository.saveAndFlush(user);

            return this.getRegisterResponse(user);
        }catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin already exists");
        }
    }

    @Override
    public LoginResponse login(AuthRequest authRequest) {
        this.validationUtil.validate(authRequest);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail().toLowerCase(),
                authRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        AppUser appUser = (AppUser) authenticate.getPrincipal();
        String token = this.jwtUtil.generateToken(appUser);

        List<String> roles = new ArrayList<>();
        for (GrantedAuthority role : appUser.getAuthorities()){
            roles.add(role.toString().split("_")[1].toLowerCase());
        }

        return LoginResponse.builder()
                .email(appUser.getEmail())
                .token(token)
                .role(roles)
                .build();
    }

    private RegisterResponse getRegisterResponse(User user){
        List<String> roles = new ArrayList<>();
        for (Role role : user.getRoles()){
            roles.add(role.getName().toString().split("_")[1].toLowerCase());
        }

        return RegisterResponse.builder()
                .email(user.getEmail())
                .role(roles)
                .build();
    }
}
