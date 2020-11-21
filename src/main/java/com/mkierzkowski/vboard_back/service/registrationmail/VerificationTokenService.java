package com.mkierzkowski.vboard_back.service.registrationmail;

import com.mkierzkowski.vboard_back.model.user.User;

public interface VerificationTokenService {
    void createVerificationToken(User user, String token);
}
