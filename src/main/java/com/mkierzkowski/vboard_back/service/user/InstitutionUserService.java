package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.model.user.RegisterInstitutionUserDto;

public interface InstitutionUserService {
    RegisterInstitutionUserDto signup(RegisterInstitutionUserDto registerInstitutionUserDto);
}
