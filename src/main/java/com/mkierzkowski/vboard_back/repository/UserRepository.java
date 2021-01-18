package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
