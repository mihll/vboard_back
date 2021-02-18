package com.mkierzkowski.vboard_back.dto.response.board.my;

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
public class MyBoardInfoResponseDto {
    Boolean wantNotifications;
    Date joinDate;
    Boolean isAdmin;
    Integer orderIndex;

    String boardId;
    Boolean isPrivate;
    String boardName;
    String description;
    Date creationDate;
    String addressCity;
    String addressPostCode;
    String addressStreet;
}
