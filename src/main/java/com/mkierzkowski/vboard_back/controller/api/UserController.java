package com.mkierzkowski.vboard_back.controller.api;

import com.mkierzkowski.vboard_back.controller.request.InstitutionUserSignupRequest;
import com.mkierzkowski.vboard_back.controller.request.PersonUserSignupRequest;
import com.mkierzkowski.vboard_back.dto.model.user.RegisterInstitutionUserDto;
import com.mkierzkowski.vboard_back.dto.model.user.RegisterPersonUserDto;
import com.mkierzkowski.vboard_back.dto.response.Response;
import com.mkierzkowski.vboard_back.service.registrationmail.VerificationTokenService;
import com.mkierzkowski.vboard_back.service.user.InstitutionUserService;
import com.mkierzkowski.vboard_back.service.user.PersonUserService;
import com.mkierzkowski.vboard_back.service.user.UserService;
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
    public Response signup(@RequestBody @Valid PersonUserSignupRequest personUserSignupRequest) {
        return Response.ok().setPayload(registerPersonUser(personUserSignupRequest));
    }

    private RegisterPersonUserDto registerPersonUser(PersonUserSignupRequest personUserSignupRequest) {
        RegisterPersonUserDto registerPersonUserDto = new RegisterPersonUserDto()
                .setEmail(personUserSignupRequest.getEmail())
                .setPassword(personUserSignupRequest.getPassword())
                .setFirstName(personUserSignupRequest.getFirstName())
                .setLastName(personUserSignupRequest.getLastName());
        return personUserService.signup(registerPersonUserDto);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/signup/institution")
    public Response signup(@RequestBody @Valid InstitutionUserSignupRequest institutionUserSignupRequest) {
        return Response.ok().setPayload(registerInstitutionUser(institutionUserSignupRequest));
    }

    private RegisterInstitutionUserDto registerInstitutionUser(InstitutionUserSignupRequest institutionUserSignupRequest) {
        RegisterInstitutionUserDto registerInstitutionUserDto = new RegisterInstitutionUserDto()
                .setEmail(institutionUserSignupRequest.getEmail())
                .setPassword(institutionUserSignupRequest.getPassword())
                .setInstitutionName(institutionUserSignupRequest.getInstitutionName());

        return institutionUserService.signup(registerInstitutionUserDto);
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/signup/confirm")
    public Response confirm(@RequestParam("token") String token) {
        return Response.ok().setPayload(verificationTokenService.verify(token));
    }

}
