package com.mkierzkowski.vboard_back.dto.response.board.my;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyBoardInfoResponseDto {
    Boolean wantNotifications;
    Date joinDate;
    Boolean isAdmin;
    Integer orderIndex;
    Integer boardMembers;
    Integer notificationsNumber;

    String boardId;
    Boolean isPrivate;
    Boolean acceptAll;
    String boardName;
    String description;
    Date createdDate;
    String addressCity;
    String addressPostCode;
    String addressStreet;
}
