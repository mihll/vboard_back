package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.model.user.UserDto;

public interface UserService {
    UserDto findUserByEmail(String email);
}
