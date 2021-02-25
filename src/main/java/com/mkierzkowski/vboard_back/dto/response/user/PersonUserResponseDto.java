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
public class PersonUserResponseDto extends UserResponseDto {
    private String firstName;
    private String lastName;
    private Date birthDate;
}
