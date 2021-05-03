package com.mkierzkowski.vboard_back.dto.response.board.joinRequests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBoardJoinRequestsResponseDto {
    List<BoardJoinRequestResponseDto> joinRequests;
}
