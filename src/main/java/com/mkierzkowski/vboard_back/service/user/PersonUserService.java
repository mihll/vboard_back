package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.model.user.RegisterPersonUserDto;

public interface PersonUserService {
    RegisterPersonUserDto signup(RegisterPersonUserDto registerPersonUserDto);
}
