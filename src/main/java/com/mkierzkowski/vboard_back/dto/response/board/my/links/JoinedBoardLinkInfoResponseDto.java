package com.mkierzkowski.vboard_back.dto.response.board.my.links;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JoinedBoardLinkInfoResponseDto {
    String boardId;
    String boardName;
    Integer notificationsCount;
}
