package com.ncs.nusiss.userservice.service;

import com.ncs.nusiss.userservice.entity.AppUser;

import java.util.List;

public interface AppUserService {
    AppUser saveUser(AppUser appUser);
    AppUser getUser(String email);
    List<AppUser> getUsers();
}
