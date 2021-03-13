package com.mkierzkowski.vboard_back.controller;

import com.mkierzkowski.vboard_back.dto.request.signup.AbstractUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.user.AbstractUserUpdateRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordChangeRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordResetRequestDto;
import com.mkierzkowski.vboard_back.dto.response.Response;
import com.mkierzkowski.vboard_back.dto.response.user.InstitutionUserResponseDto;
import com.mkierzkowski.vboard_back.dto.response.user.PersonUserResponseDto;
import com.mkierzkowski.vboard_back.dto.response.user.UserResponseDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.InstitutionUser;
import com.mkierzkowski.vboard_back.model.user.PersonUser;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.service.user.UserService;
import com.mkierzkowski.vboard_back.service.user.verification.VerificationTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    VerificationTokenService verificationTokenService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/me")
    public ResponseEntity getMyDetails() {
        User currentUser = userService.getCurrentUser();
        UserResponseDto responseDto = prepareUserResponse(currentUser);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/me")
    public ResponseEntity updateUser(@RequestBody @Valid AbstractUserUpdateRequestDto userUpdateRequestDto) {
        User updatedUser = userService.update(userUpdateRequestDto);
        UserResponseDto responseDto = prepareUserResponse(updatedUser);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/changeProfilePic")
    public ResponseEntity changeProfilePic(@RequestParam(value = "profilePic", required = false) MultipartFile profilePic) {
        User updatedUser = userService.changeProfilePic(profilePic);
        UserResponseDto responseDto = prepareUserResponse(updatedUser);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/signup")
    public Response signup(@RequestBody @Valid AbstractUserSignupRequestDto userSignupRequestDto) {
        userService.signup(userSignupRequestDto);
        return Response.ok();
    }

    @GetMapping("/signup/confirm")
    public Response confirm(@RequestParam("token") String token) {
        userService.verifyUserForToken(token);
        return Response.ok();
    }

    @PostMapping("/resetPassword")
    public Response resetPassword(@RequestBody @Valid UserPasswordResetRequestDto userPasswordResetRequestDto) {
        userService.resetPassword(userPasswordResetRequestDto);
        return Response.ok();
    }

    @PostMapping("/changePassword")
    public Response changePassword(@RequestBody @Valid UserPasswordChangeRequestDto userPasswordChangeRequestDto) {
        userService.changePassword(userPasswordChangeRequestDto);
        return Response.ok();
    }

    @PostMapping("/logout")
    public Response logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0); // setting cookie age to 0 to delete it from client
        cookie.setHttpOnly(true);
        cookie.setPath("/refresh");

        response.addCookie(cookie);

        return Response.ok();
    }

    private UserResponseDto prepareUserResponse(User user) {
        UserResponseDto responseDto;
        if (user instanceof PersonUser) {
            responseDto = modelMapper.map(user, PersonUserResponseDto.class);
        } else if (user instanceof InstitutionUser) {
            responseDto = modelMapper.map(user, InstitutionUserResponseDto.class);
        } else {
            throw VBoardException.throwException(EntityType.USER, ExceptionType.INVALID);
        }
        return responseDto;
    }
}
