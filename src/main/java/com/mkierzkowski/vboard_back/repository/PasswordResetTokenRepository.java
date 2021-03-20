package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.token.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
}
