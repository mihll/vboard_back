package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordChangeRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordResetRequestDto;
import com.mkierzkowski.vboard_back.model.user.User;

public interface UserService {

    void deleteUser(User user);

    void verifyUserForToken(String token);

    void resetPassword(UserPasswordResetRequestDto userPasswordResetRequestDto);

    void changePassword(UserPasswordChangeRequestDto UserPasswordChangeRequestDto, String token);
}
