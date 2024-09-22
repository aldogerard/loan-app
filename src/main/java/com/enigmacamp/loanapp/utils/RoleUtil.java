package com.enigmacamp.loanapp.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.StringJoiner;

public class RoleUtil {
    public static String getName(Authentication auth) {
        StringJoiner role = new StringJoiner(", ");
        for (GrantedAuthority authority : auth.getAuthorities()) {
            role.add(authority.getAuthority().split("_")[1].toLowerCase());
        }
        return role.toString();
    }
}
