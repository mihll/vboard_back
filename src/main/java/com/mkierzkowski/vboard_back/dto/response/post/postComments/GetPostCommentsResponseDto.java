package com.mkierzkowski.vboard_back.dto.response.post.postComments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPostCommentsResponseDto {
    List<PostCommentResponseDto> comments;
}
