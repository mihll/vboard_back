package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.request.signup.InstitutionUserSignupRequestDto;

public interface InstitutionUserService {
    void signup(InstitutionUserSignupRequestDto institutionUserSignupRequestDto);
}
