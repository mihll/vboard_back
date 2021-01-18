package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.token.VerificationToken;
import com.mkierzkowski.vboard_back.model.user.User;
import org.springframework.data.repository.CrudRepository;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
