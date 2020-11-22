package com.mkierzkowski.vboard_back.dto.mapper.user;

import com.mkierzkowski.vboard_back.dto.model.user.VerificationDto;
import com.mkierzkowski.vboard_back.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class VerificationMapper {
    public static VerificationDto toVerificationDto(User user) {
        return new VerificationDto()
                .setEmail(user.getEmail());
    }
}
