package com.mkierzkowski.vboard_back.controller;

import com.mkierzkowski.vboard_back.dto.request.signup.AbstractUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.user.AbstractUserUpdateRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordChangeRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordResetRequestDto;
import com.mkierzkowski.vboard_back.dto.response.user.InstitutionUserResponseDto;
import com.mkierzkowski.vboard_back.dto.response.user.PersonUserResponseDto;
import com.mkierzkowski.vboard_back.dto.response.user.UserResponseDto;
import com.mkierzkowski.vboard_back.model.user.InstitutionUser;
import com.mkierzkowski.vboard_back.model.user.PersonUser;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyDetails() {
        User currentUser = userService.getCurrentUser();
        UserResponseDto responseDto = prepareUserResponse(currentUser);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody @Valid AbstractUserUpdateRequestDto userUpdateRequestDto) {
        User updatedUser = userService.update(userUpdateRequestDto);
        UserResponseDto responseDto = prepareUserResponse(updatedUser);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUserAccount() {
        userService.deleteUserAccount();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/changeProfilePic")
    public ResponseEntity<UserResponseDto> changeProfilePic(@RequestParam(value = "profilePic", required = false) MultipartFile profilePic) {
        User updatedUser = userService.changeProfilePic(profilePic);
        UserResponseDto responseDto = prepareUserResponse(updatedUser);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid AbstractUserSignupRequestDto userSignupRequestDto) {
        userService.signup(userSignupRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        userService.verifyUserForToken(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid UserPasswordResetRequestDto userPasswordResetRequestDto) {
        userService.resetPassword(userPasswordResetRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody @Valid UserPasswordChangeRequestDto userPasswordChangeRequestDto) {
        userService.changePassword(userPasswordChangeRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0); // setting cookie age to 0 to delete it from client
        cookie.setHttpOnly(true);
        cookie.setPath("/refresh");

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    private UserResponseDto prepareUserResponse(User user) {
        UserResponseDto responseDto;
        if (user instanceof PersonUser) {
            responseDto = modelMapper.map(user, PersonUserResponseDto.class);
        } else if (user instanceof InstitutionUser) {
            responseDto = modelMapper.map(user, InstitutionUserResponseDto.class);
        } else {
            throw new RuntimeException("Could not prepare user of type: " + user.getClass().getSimpleName());
        }
        return responseDto;
    }
}
