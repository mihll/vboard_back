package com.mkierzkowski.vboard_back.dto.response.board.my.links;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetMyBoardsLinksResponseDto {
    List<JoinedBoardLinkInfoResponseDto> boardLinks;
}
