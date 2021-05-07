package com.mkierzkowski.vboard_back.dto.response.post.boardPosts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardPostResponseDto {
    String userId;
    String userName;
    String profilePicUrl;
    Boolean isAdmin;

    String postId;
    String postText;
    Boolean isPinned;
}
