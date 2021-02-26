package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.request.signup.AbstractUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.user.AbstractUserUpdateRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordChangeRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordResetRequestDto;
import com.mkierzkowski.vboard_back.model.user.User;

public interface UserService {

    void signup(AbstractUserSignupRequestDto userSignupRequestDto);

    User update(AbstractUserUpdateRequestDto userUpdateRequestDto);

    User findUserByEmail(String email);

    User findUserById(Long id);

    User getCurrentUser();

    void deleteUser(User user);

    User saveAndFlushUser(User user);

    void verifyUserForToken(String token);

    void resetPassword(UserPasswordResetRequestDto userPasswordResetRequestDto);

    void changePassword(UserPasswordChangeRequestDto UserPasswordChangeRequestDto);
}
