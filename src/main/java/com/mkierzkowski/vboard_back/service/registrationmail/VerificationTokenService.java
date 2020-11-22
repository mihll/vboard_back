package com.mkierzkowski.vboard_back.service.registrationmail;

import com.mkierzkowski.vboard_back.dto.model.user.VerificationDto;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.model.user.VerificationToken;

public interface VerificationTokenService {
    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String verificationToken);

    VerificationDto verify(String token);
}
