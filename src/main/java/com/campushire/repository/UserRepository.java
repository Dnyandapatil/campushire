package com.campushire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.campushire.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
