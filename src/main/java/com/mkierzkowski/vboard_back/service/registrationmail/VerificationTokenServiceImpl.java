package com.mkierzkowski.vboard_back.service.registrationmail;

import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.model.user.VerificationToken;
import com.mkierzkowski.vboard_back.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenServiceImpl implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }
}
