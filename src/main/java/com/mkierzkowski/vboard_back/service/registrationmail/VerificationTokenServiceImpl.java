package com.mkierzkowski.vboard_back.service.registrationmail;

import com.mkierzkowski.vboard_back.dto.model.user.VerificationDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.model.user.VerificationToken;
import com.mkierzkowski.vboard_back.repository.VerificationTokenRepository;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Calendar;

@Component
public class VerificationTokenServiceImpl implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserService userService;

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }

    @Override
    public void deleteVerificationToken(VerificationToken verificationToken) {
        tokenRepository.delete(verificationToken);
    }

    @Override
    public Iterable<VerificationToken> getAllVerificationTokens() {
        return tokenRepository.findAll();
    }

    @Override
    @Transactional
    public VerificationDto verify(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw exception(EntityType.VERIFICATION_TOKEN, ExceptionType.ENTITY_NOT_FOUND, token);
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            userService.deleteUser(user);
            throw exception(EntityType.VERIFICATION_TOKEN, ExceptionType.EXPIRED, token);
        }
        tokenRepository.delete(verificationToken);
        return userService.verifyRegisteredUser(user);
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return VBoardException.throwException(entityType, exceptionType, args);
    }
}
