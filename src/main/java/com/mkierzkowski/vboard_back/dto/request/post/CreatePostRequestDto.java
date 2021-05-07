package com.mkierzkowski.vboard_back.dto.request.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePostRequestDto {

    @NotNull(message = "{constraints.NotNull.message}")
    Long boardId;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    String postText;
}
