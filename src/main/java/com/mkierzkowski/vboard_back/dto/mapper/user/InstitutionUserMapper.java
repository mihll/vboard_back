package com.mkierzkowski.vboard_back.dto.mapper.user;

import com.mkierzkowski.vboard_back.dto.model.user.RegisterInstitutionUserDto;
import com.mkierzkowski.vboard_back.model.user.InstitutionUser;
import org.springframework.stereotype.Component;

@Component
public class InstitutionUserMapper {

    public static RegisterInstitutionUserDto toRegisterInstitutionUserDto(InstitutionUser institutionUser) {
        return new RegisterInstitutionUserDto()
                .setEmail(institutionUser.getEmail())
                .setInstitutionName(institutionUser.getInstitutionName());
    }
}
