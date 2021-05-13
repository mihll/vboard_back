package com.mkierzkowski.vboard_back.model.post;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostComment extends Auditable<String> {

    @Id
    @GeneratedValue
    Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "board_id"),
            @JoinColumn(name = "user_id"),
    })
    BoardMember boardMember;

    @NotBlank
    @Column(name = "commentText", columnDefinition = "LONGTEXT")
    String commentText;

    public PostComment(Post post, BoardMember boardMember, String commentText) {
        this.post = post;
        this.boardMember = boardMember;
        this.commentText = commentText;

        boardMember.getMemberPostComments().add(this);
        post.getPostComments().add(this);
    }

    public PostComment() {

    }
}
