package com.mkierzkowski.vboard_back.controller;

import com.mkierzkowski.vboard_back.dto.request.signup.InstitutionUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.signup.PersonUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordChangeRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordResetRequestDto;
import com.mkierzkowski.vboard_back.dto.response.Response;
import com.mkierzkowski.vboard_back.service.user.InstitutionUserService;
import com.mkierzkowski.vboard_back.service.user.PersonUserService;
import com.mkierzkowski.vboard_back.service.user.UserService;
import com.mkierzkowski.vboard_back.service.user.verification.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @SuppressWarnings("rawtypes")
    @PostMapping("/signup/person")
    public Response signup(@RequestBody @Valid PersonUserSignupRequestDto personUserSignupRequestDto) {
        personUserService.signup(personUserSignupRequestDto);
        return Response.ok();
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/signup/institution")
    public Response signup(@RequestBody @Valid InstitutionUserSignupRequestDto institutionUserSignupRequestDto) {
        institutionUserService.signup(institutionUserSignupRequestDto);
        return Response.ok();
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/signup/confirm")
    public Response confirm(@RequestParam("token") String token) {
        userService.verifyUserForToken(token);
        return Response.ok();
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/resetPassword")
    public Response resetPassword(@RequestBody @Valid UserPasswordResetRequestDto userPasswordResetRequestDto) {
        userService.resetPassword(userPasswordResetRequestDto);
        return Response.ok();
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/changePassword")
    public Response changePassword(@RequestBody @Valid UserPasswordChangeRequestDto userPasswordChangeRequestDto, @RequestParam("token") String token) {
        userService.changePassword(userPasswordChangeRequestDto, token);
        return Response.ok();
    }
}
