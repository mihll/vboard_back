package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.request.signup.InstitutionUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.signup.PersonUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.user.InstitutionUserUpdateRequestDto;
import com.mkierzkowski.vboard_back.dto.request.user.PersonUserUpdateRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordChangeRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordResetRequestDto;
import com.mkierzkowski.vboard_back.model.user.InstitutionUser;
import com.mkierzkowski.vboard_back.model.user.PersonUser;
import com.mkierzkowski.vboard_back.model.user.User;

public interface UserService {

    void signup(PersonUserSignupRequestDto personUserSignupRequestDto);

    void signup(InstitutionUserSignupRequestDto institutionUserSignupRequestDto);

    PersonUser update(PersonUserUpdateRequestDto personUserUpdateRequestDto);

    InstitutionUser update(InstitutionUserUpdateRequestDto institutionUserUpdateRequestDto);

    User findUserByEmail(String email);

    User findUserById(Long id);

    User getCurrentUser();

    void deleteUser(User user);

    User saveAndFlushUser(User user);

    void verifyUserForToken(String token);

    void resetPassword(UserPasswordResetRequestDto userPasswordResetRequestDto);

    void changePassword(UserPasswordChangeRequestDto UserPasswordChangeRequestDto, String token);
}
