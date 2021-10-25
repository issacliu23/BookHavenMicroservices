package com.ncs.nusiss.bookservice.securityconfig;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUtils {
    public static String getUsernameFromJwt() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
