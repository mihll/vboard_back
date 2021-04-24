package com.mkierzkowski.vboard_back.dto.response.board.members;

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
public class BoardMemberInfoResponseDto {
    String userId;
    String name;
    String profilePicUrl;

    Boolean isAdmin;
    Boolean didLeft;
    Date joinDate;
    int postsNumber;
}
