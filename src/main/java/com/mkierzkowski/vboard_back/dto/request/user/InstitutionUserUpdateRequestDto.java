package com.mkierzkowski.vboard_back.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstitutionUserUpdateRequestDto extends AbstractUserUpdateRequestDto {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String institutionName;

    private String addressCity;

    private String addressPostCode;

    private String addressStreet;
}
