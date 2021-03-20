package com.mkierzkowski.vboard_back.dto.request.userpassword;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mkierzkowski.vboard_back.validation.ValidPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPasswordChangeRequestDto {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @ValidPassword
    private String newPassword;

    private String currentPassword;

    private String token;
}
