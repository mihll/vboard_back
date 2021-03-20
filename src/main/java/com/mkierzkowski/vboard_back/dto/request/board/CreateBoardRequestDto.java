package com.mkierzkowski.vboard_back.dto.request.board;

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
public class CreateBoardRequestDto {

    @NotNull(message = "{constraints.NotNull.message}")
    Boolean isPrivate;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    String boardName;

    String description;

    String addressCity;

    String addressPostCode;

    String addressStreet;
}
