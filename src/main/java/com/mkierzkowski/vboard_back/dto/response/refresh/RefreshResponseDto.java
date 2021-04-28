package com.mkierzkowski.vboard_back.dto.response.refresh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshResponseDto {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String userId;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String accessToken;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String name;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String profilePicUrl;
}