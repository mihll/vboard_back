package com.mkierzkowski.vboard_back.model.post;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class PostLikeKey implements Serializable {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "board_id")
    Long boardId;

    @Column(name = "post_id")
    Long postId;

    public PostLikeKey(Long userId, Long boardId, Long postId) {
        this.userId = userId;
        this.boardId = boardId;
        this.postId = postId;
    }

    public PostLikeKey() {
    }
}
