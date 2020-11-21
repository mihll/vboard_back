package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.model.user.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
