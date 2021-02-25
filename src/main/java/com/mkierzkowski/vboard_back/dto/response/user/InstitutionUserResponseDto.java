package com.mkierzkowski.vboard_back.dto.response.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstitutionUserResponseDto extends UserResponseDto {
    private String institutionName;
    private String addressCity;
    private String addressPostCode;
    private String addressStreet;
}
