package com.mkierzkowski.vboard_back.dto.request.signup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mkierzkowski.vboard_back.validation.ValidPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstitutionUserSignupRequestDto {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @Email
    private String email;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @ValidPassword
    private String password;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String institutionName;
}
