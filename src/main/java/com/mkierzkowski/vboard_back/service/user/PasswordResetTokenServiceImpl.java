package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.model.user.UserPasswordResetDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.PasswordResetToken;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserPasswordResetDto createPasswordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
        return new UserPasswordResetDto().setEmail(user.getEmail());
    }

    @Override
    public Iterable<PasswordResetToken> getAllPasswordResetTokens() {
        return passwordResetTokenRepository.findAll();
    }

    @Override
    public void deletePasswordResetToken(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    @Override
    public User getUserForValidPasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            throw exception(EntityType.PASSWORD_RESET_TOKEN, ExceptionType.ENTITY_NOT_FOUND, token);
        }
        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw exception(EntityType.VERIFICATION_TOKEN, ExceptionType.EXPIRED, token);
        }
        passwordResetTokenRepository.delete(passwordResetToken);
        return user;
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return VBoardException.throwException(entityType, exceptionType, args);
    }
}
