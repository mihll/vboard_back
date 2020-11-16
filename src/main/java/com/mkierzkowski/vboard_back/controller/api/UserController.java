package com.mkierzkowski.vboard_back.controller.api;

import com.mkierzkowski.vboard_back.controller.request.PersonUserSignupRequest;
import com.mkierzkowski.vboard_back.dto.model.user.RegisterPersonUserDto;
import com.mkierzkowski.vboard_back.dto.response.Response;
import com.mkierzkowski.vboard_back.service.user.PersonUserService;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PersonUserService personUserService;

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

}
