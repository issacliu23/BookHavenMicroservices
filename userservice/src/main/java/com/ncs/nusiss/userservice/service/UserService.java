package com.ncs.nusiss.userservice.service;

import com.ncs.nusiss.userservice.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User getUser(String email);
    List<User> getUsers();
}
