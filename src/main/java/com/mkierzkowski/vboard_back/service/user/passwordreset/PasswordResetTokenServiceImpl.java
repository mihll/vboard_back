package com.mkierzkowski.vboard_back.service.user.passwordreset;

import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.token.PasswordResetToken;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public void deletePasswordResetToken(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    @Override
    public Iterable<PasswordResetToken> getAllPasswordResetTokens() {
        return passwordResetTokenRepository.findAll();
    }

    @Override
    public User getUserForValidPasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                                                        .orElseThrow(() -> VBoardException.throwException(EntityType.PASSWORD_RESET_TOKEN, ExceptionType.ENTITY_NOT_FOUND, token));

        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw VBoardException.throwException(EntityType.VERIFICATION_TOKEN, ExceptionType.EXPIRED, token);
        }

        passwordResetTokenRepository.delete(passwordResetToken);

        return user;
    }
}
