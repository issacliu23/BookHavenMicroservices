package com.ncs.nusiss.userservice.service;

import com.ncs.nusiss.userservice.entity.AppUser;
import com.ncs.nusiss.userservice.entity.ConfirmationToken;
import com.ncs.nusiss.userservice.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final EmailValidator emailValidator;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

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
    /*
    public AppUser signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()) != null ? true : false;
        if(userExists) {
            throw new IllegalStateException("email already taken");
        }
        log.info("Signing up new User {} to the Database", appUser.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }*/
    public String signUpUser(AppUser appUser) {
        boolean isValidEmail = emailValidator.test(appUser.getEmail());
        if(!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()) != null ? true : false;
        if(userExists) {
            throw new IllegalStateException("email already taken");
        }

        log.info("Signing up new User {} to the Database", appUser.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // TODO: SEND EMAIL

        return token;
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
