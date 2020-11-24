package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);
}
