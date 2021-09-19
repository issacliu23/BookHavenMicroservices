package com.ncs.nusiss.userservice.service;

import com.ncs.nusiss.userservice.entity.AppUser;
import com.ncs.nusiss.userservice.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(email);
        if(appUser == null) {
            log.error("User not found in the Database");
            throw new UsernameNotFoundException("User not found in the Database");
        } else {
            log.info("User found in the Database: {}", email);
        }
        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(), appUser.getAuthorities());
    }

    @Override
    public AppUser signUpUser(AppUser appUser) {
        log.info("Signing up new User {} to the Database", appUser.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser getUser(String email) {
        log.info("Fetching User {} from the Database", email);
        return appUserRepository.findByEmail(email);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all Users from the Database");
        return appUserRepository.findAll();
    }
}
