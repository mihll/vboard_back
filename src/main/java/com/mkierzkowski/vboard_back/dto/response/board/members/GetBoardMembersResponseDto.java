package com.mkierzkowski.vboard_back.dto.response.board.members;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBoardMembersResponseDto {
    List<BoardMemberInfoResponseDto> members;
}
