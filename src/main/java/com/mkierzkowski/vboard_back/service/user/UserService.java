package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.model.user.*;
import com.mkierzkowski.vboard_back.model.user.User;

public interface UserService {
    UserDto findUserByEmail(String email);

    void deleteUser(User user);

    VerificationDto verifyRegisteredUser(User user);

    UserPasswordResetDto resetPassword(UserPasswordResetDto userPasswordResetDto);

    UserPasswordChangedDto changePassword(UserPasswordChangeDto userPasswordChangeDto);
}
