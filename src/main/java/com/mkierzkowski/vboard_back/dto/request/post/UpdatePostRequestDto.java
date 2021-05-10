package com.mkierzkowski.vboard_back.dto.request.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePostRequestDto {
    @NotEmpty(message = "{constraints.NotEmpty.message}")
    String postText;
}
