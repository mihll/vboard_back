package com.mkierzkowski.vboard_back.dto.mapper.user;

import com.mkierzkowski.vboard_back.dto.model.user.UserPasswordChangedDto;
import com.mkierzkowski.vboard_back.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordChangedMapper {
    public static UserPasswordChangedDto toUserPasswordChangedDto(User user) {
        return new UserPasswordChangedDto()
                .setEmail(user.getEmail());
    }
}
