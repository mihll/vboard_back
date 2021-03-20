package com.mkierzkowski.vboard_back.dto.response.board.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBoardsResponseDto {
    List<BoardInfoResponseDto> boards;
}
