package com.mkierzkowski.vboard_back.dto.request.userpassword;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPasswordResetRequestDto {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String email;
}
