package com.mkierzkowski.vboard_back.service.user.passwordreset;

import com.mkierzkowski.vboard_back.model.token.PasswordResetToken;
import com.mkierzkowski.vboard_back.model.user.User;

public interface PasswordResetTokenService {

    PasswordResetToken createPasswordResetToken(User user);

    void deletePasswordResetToken(PasswordResetToken passwordResetToken);

    Iterable<PasswordResetToken> getAllPasswordResetTokens();

    User getUserForValidPasswordResetToken(String token);

}
