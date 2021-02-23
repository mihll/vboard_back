package com.mkierzkowski.vboard_back.dto.response.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto {
    private String email;
    private Date signupDate;
    private String profileImgUrl;

    private String firstName;
    private String lastName;
    private Date birthDate;

    private String institutionName;
    private String addressCity;
    private String addressPostCode;
    private String addressStreet;
}
