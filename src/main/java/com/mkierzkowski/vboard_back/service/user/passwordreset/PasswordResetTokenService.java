package com.mkierzkowski.vboard_back.service.user.passwordreset;

import com.mkierzkowski.vboard_back.model.user.PasswordResetToken;
import com.mkierzkowski.vboard_back.model.user.User;

public interface PasswordResetTokenService {

    void createPasswordResetToken(User user, String token);

    void deletePasswordResetToken(PasswordResetToken passwordResetToken);

    Iterable<PasswordResetToken> getAllPasswordResetTokens();

    User getUserForValidPasswordResetToken(String token);

}
