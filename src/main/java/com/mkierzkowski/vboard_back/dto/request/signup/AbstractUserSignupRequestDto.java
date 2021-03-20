package com.mkierzkowski.vboard_back.dto.request.signup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mkierzkowski.vboard_back.validation.ValidPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "userType")
@JsonSubTypes({@JsonSubTypes.Type(value = PersonUserSignupRequestDto.class, name = "person"),
        @JsonSubTypes.Type(value = InstitutionUserSignupRequestDto.class, name = "institution")})
public abstract class AbstractUserSignupRequestDto {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String email;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @ValidPassword
    private String password;

    private String userType;
}
