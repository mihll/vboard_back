package com.mkierzkowski.vboard_back.dto.response.board;

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
public class BoardInfoResponseDto {
    String boardId;
    Boolean isPrivate;
    String boardName;
    String description;
    Date creationDate;
    String addressCity;
    String addressPostCode;
    String addressStreet;
}
