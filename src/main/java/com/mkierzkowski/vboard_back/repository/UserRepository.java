package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
