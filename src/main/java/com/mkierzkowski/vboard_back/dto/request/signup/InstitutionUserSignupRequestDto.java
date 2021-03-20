package com.mkierzkowski.vboard_back.dto.request.signup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstitutionUserSignupRequestDto extends AbstractUserSignupRequestDto {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String institutionName;
}
