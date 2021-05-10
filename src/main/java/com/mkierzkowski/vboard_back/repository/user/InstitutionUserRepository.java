package com.mkierzkowski.vboard_back.repository.user;

import com.mkierzkowski.vboard_back.model.user.InstitutionUser;
import org.springframework.data.repository.CrudRepository;

public interface InstitutionUserRepository extends CrudRepository<InstitutionUser, Long> {
}
