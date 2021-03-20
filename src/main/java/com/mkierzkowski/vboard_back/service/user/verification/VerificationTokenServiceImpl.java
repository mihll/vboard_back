package com.mkierzkowski.vboard_back.service.user.verification;

import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.token.VerificationToken;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.VerificationTokenRepository;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    @Transactional
    public VerificationToken createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken myToken = new VerificationToken(token, user);
        return tokenRepository.save(myToken);
    }

    @Override
    @Transactional
    public Optional<VerificationToken> getVerificationTokenForUser(User user) {
        return tokenRepository.findByUser(user);
    }

    @Override
    @Transactional
    public void deleteVerificationToken(VerificationToken verificationToken) {
        tokenRepository.delete(verificationToken);
    }

    @Override
    public Iterable<VerificationToken> getAllVerificationTokens() {
        return tokenRepository.findAll();
    }

    @Override
    public User getUserForValidVerificationToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                                                      .orElseThrow(() -> VBoardException.throwException(EntityType.VERIFICATION_TOKEN, ExceptionType.ENTITY_NOT_FOUND, token));

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            userService.deleteUser(user);
            throw VBoardException.throwException(EntityType.VERIFICATION_TOKEN, ExceptionType.EXPIRED, token);
        }

        tokenRepository.delete(verificationToken);

        return user;
    }
}
