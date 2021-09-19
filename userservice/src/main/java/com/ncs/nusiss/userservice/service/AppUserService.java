package com.ncs.nusiss.userservice.service;

import com.ncs.nusiss.userservice.entity.AppUser;

import java.util.List;

public interface AppUserService {
    // AppUser signUpUser(AppUser appUser);
    String signUpUser(AppUser appUser);
    AppUser getUser(String email);
    List<AppUser> getUsers();
    String confirmToken(String token);
}
