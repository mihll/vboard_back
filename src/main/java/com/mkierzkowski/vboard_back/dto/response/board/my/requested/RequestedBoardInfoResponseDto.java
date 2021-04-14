package com.mkierzkowski.vboard_back.dto.response.board.my.requested;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestedBoardInfoResponseDto {
    Date joinRequestDate;

    String boardId;
    Boolean isPrivate;
    String boardName;
}
