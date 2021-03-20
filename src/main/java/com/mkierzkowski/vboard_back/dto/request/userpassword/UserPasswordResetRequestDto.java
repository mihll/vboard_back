package com.mkierzkowski.vboard_back.dto.request.userpassword;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPasswordResetRequestDto {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @Email
    private String email;
}
