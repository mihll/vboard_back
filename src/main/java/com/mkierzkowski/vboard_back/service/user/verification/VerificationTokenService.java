package com.mkierzkowski.vboard_back.service.user.verification;

import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.model.user.VerificationToken;

public interface VerificationTokenService {

    void createVerificationToken(User user, String token);

    void deleteVerificationToken(VerificationToken verificationToken);

    Iterable<VerificationToken> getAllVerificationTokens();

    User getUserForValidVerificationToken(String token);
}
