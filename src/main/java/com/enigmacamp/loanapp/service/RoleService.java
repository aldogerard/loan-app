package com.enigmacamp.loanapp.service;

import com.enigmacamp.loanapp.entity.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    Role getOrCreate(Role role);
}
