package com.mkierzkowski.vboard_back.dto.response.board.joinRequests;

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
public class BoardJoinRequestResponseDto {
    String userId;
    String name;
    String profilePicUrl;
    Date requestDate;
}
