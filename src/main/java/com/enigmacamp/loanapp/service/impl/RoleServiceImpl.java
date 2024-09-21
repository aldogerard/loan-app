package com.enigmacamp.loanapp.service.impl;

import com.enigmacamp.loanapp.entity.Role;
import com.enigmacamp.loanapp.repository.RoleRepository;
import com.enigmacamp.loanapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrCreate(Role role) {
        Optional<Role> findRole = this.roleRepository.findByName(role.getName());
        return findRole.orElseGet(() -> this.roleRepository.save(role));
    }
}
