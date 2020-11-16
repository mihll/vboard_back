package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.user.PersonUser;
import org.springframework.data.repository.CrudRepository;

public interface PersonUserRepository extends CrudRepository<PersonUser, Long> {
}
