package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.token.VerificationToken;
import com.mkierzkowski.vboard_back.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUser(User user);
}
