package com.enigmacamp.loanapp.service.impl;

import com.enigmacamp.loanapp.constant.enums.ERole;
import com.enigmacamp.loanapp.entity.AppUser;
import com.enigmacamp.loanapp.entity.Role;
import com.enigmacamp.loanapp.entity.User;
import com.enigmacamp.loanapp.repository.UserRepository;
import com.enigmacamp.loanapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // VERIFIKASI JWT
    @Override
    public AppUser loadUserByUserId(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        List<ERole> role = new ArrayList<>();
        for (Role roleEnum : user.getRoles()) {
            role.add(roleEnum.getName());
        }
        return AppUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(role)
                .build();
    }

    @Override
    public void deleteUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        userRepository.deleteById(user.getId());
    }

    // VERIFIKASI AUTENTIFIKASI LOGIN
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        List<ERole> role = new ArrayList<>();
        for (Role roleEnum : user.getRoles()) {
            role.add(roleEnum.getName());
        }

        return AppUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(role)
                .build();
    }


}

