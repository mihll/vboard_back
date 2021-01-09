package com.mkierzkowski.vboard_back.repository;

import com.mkierzkowski.vboard_back.model.user.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);
}