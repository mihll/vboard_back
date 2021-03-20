package com.mkierzkowski.vboard_back.dto.response.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto {
    private String email;
    private Date signupDate;
    private String profilePicUrl;
    private String userType;
}
