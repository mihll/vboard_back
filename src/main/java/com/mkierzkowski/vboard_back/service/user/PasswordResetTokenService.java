package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.model.user.UserPasswordResetDto;
import com.mkierzkowski.vboard_back.model.user.PasswordResetToken;
import com.mkierzkowski.vboard_back.model.user.User;

public interface PasswordResetTokenService {
    Iterable<PasswordResetToken> getAllPasswordResetTokens();

    void deletePasswordResetToken(PasswordResetToken passwordResetToken);

    UserPasswordResetDto createPasswordResetToken(User user, String token);

    User getUserForValidPasswordResetToken(String token);

}
