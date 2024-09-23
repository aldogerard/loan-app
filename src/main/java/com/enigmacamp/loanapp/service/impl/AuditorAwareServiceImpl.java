package com.enigmacamp.loanapp.service.impl;

import com.enigmacamp.loanapp.service.AuditorAwareService;
import com.enigmacamp.loanapp.utils.CurrentUserUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareServiceImpl implements AuditorAwareService {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = CurrentUserUtil.getEmail();
            if (email == null) return Optional.of(authentication.getName());
            return Optional.of(email);
        }
        return Optional.of("system");
    }
}
