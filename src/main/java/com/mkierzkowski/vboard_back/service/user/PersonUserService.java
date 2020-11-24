package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.request.signup.PersonUserSignupRequestDto;

public interface PersonUserService {
    void signup(PersonUserSignupRequestDto personUserSignupRequestDto);
}
