package com.mkierzkowski.vboard_back.service.user.verification;

import com.mkierzkowski.vboard_back.model.token.VerificationToken;
import com.mkierzkowski.vboard_back.model.user.User;

import java.util.Optional;

public interface VerificationTokenService {

    VerificationToken createVerificationToken(User user);

    Optional<VerificationToken> getVerificationTokenForUser(User user);

    void deleteVerificationToken(VerificationToken verificationToken);

    Iterable<VerificationToken> getAllVerificationTokens();

    User getUserForValidVerificationToken(String token);
}
