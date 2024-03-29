package com.mkierzkowski.vboard_back.dto.response.board.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardInfoResponseDto {
    String boardId;
    Boolean isPrivate;
    Boolean acceptAll;
    String boardName;
    String description;
    Date createdDate;
    String addressCity;
    String addressPostCode;
    String addressStreet;

    Boolean isJoined = false;
    Boolean isRequested = false;
}
