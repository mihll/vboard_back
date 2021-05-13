package com.mkierzkowski.vboard_back.model.post;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostLike extends Auditable<String> {

    @EmbeddedId
    PostLikeKey id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "user_id", insertable = false, updatable = false),
    })
    BoardMember boardMember;

    @ManyToOne
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    Post post;

    public PostLike(BoardMember boardMember, Post post) {
        this.id = new PostLikeKey(boardMember.getId().getUserId(), boardMember.getId().getBoardId(), post.getPostId());

        this.boardMember = boardMember;
        this.post = post;

        boardMember.getMemberPostLikes().add(this);
        post.getPostLikes().add(this);
    }

    public PostLike() {

    }
}
