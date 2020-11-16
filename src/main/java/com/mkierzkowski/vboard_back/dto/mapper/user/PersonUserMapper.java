package com.mkierzkowski.vboard_back.dto.mapper.user;

import com.mkierzkowski.vboard_back.dto.model.user.RegisterPersonUserDto;
import com.mkierzkowski.vboard_back.model.user.PersonUser;
import org.springframework.stereotype.Component;

@Component
public class PersonUserMapper {

    public static RegisterPersonUserDto toRegisterPersonUserDto(PersonUser personUser) {
        return new RegisterPersonUserDto()
                .setEmail(personUser.getEmail())
                .setFirstName(personUser.getFirstName())
                .setLastName(personUser.getLastName());
    }
}
