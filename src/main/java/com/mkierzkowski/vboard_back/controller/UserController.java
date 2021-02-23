package com.mkierzkowski.vboard_back.controller;

import com.mkierzkowski.vboard_back.dto.request.signup.InstitutionUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.signup.PersonUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordChangeRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordResetRequestDto;
import com.mkierzkowski.vboard_back.dto.response.Response;
import com.mkierzkowski.vboard_back.dto.response.user.UserResponseDto;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.service.user.InstitutionUserService;
import com.mkierzkowski.vboard_back.service.user.PersonUserService;
import com.mkierzkowski.vboard_back.service.user.UserService;
import com.mkierzkowski.vboard_back.service.user.verification.VerificationTokenService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    PersonUserService personUserService;

    @Autowired
    InstitutionUserService institutionUserService;

    @Autowired
    VerificationTokenService verificationTokenService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/me")
    public ResponseEntity getMyDetails() {
        User currentUser = userService.getCurrentUser();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        UserResponseDto responseDto = modelMapper.map(currentUser, UserResponseDto.class);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/signup/person")
    public Response signup(@RequestBody @Valid PersonUserSignupRequestDto personUserSignupRequestDto) {
        personUserService.signup(personUserSignupRequestDto);
        return Response.ok();
    }

    @PostMapping("/signup/institution")
    public Response signup(@RequestBody @Valid InstitutionUserSignupRequestDto institutionUserSignupRequestDto) {
        institutionUserService.signup(institutionUserSignupRequestDto);
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
    public Response changePassword(@RequestBody @Valid UserPasswordChangeRequestDto userPasswordChangeRequestDto, @RequestParam("token") String token) {
        userService.changePassword(userPasswordChangeRequestDto, token);
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
}
