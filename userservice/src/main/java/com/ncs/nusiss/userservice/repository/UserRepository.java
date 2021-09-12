package com.ncs.nusiss.userservice.repository;

import com.ncs.nusiss.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
