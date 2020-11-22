package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.model.user.UserDto;
import com.mkierzkowski.vboard_back.dto.model.user.VerificationDto;
import com.mkierzkowski.vboard_back.model.user.User;

public interface UserService {
    UserDto findUserByEmail(String email);

    VerificationDto verifyRegisteredUser(User user);
}
