package com.mkierzkowski.vboard_back.model.post;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post extends Auditable<String> {

    @Id
    @GeneratedValue
    Long postId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "board_id"),
            @JoinColumn(name = "user_id")
    })
    BoardMember boardMember;

    @ManyToOne
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    Board board;

    @NotNull
    Boolean isPinned = false;

    @NotBlank
    @Column(name = "postText", columnDefinition = "LONGTEXT")
    String postText;

    @OneToMany(mappedBy = "post")
    List<PostLike> postLikes;

    @OneToMany(mappedBy = "post")
    List<PostComment> postComments;

    public Post(BoardMember boardMember, String postText) {
        this.boardMember = boardMember;
        this.board = boardMember.getBoard();
        this.postText = postText;

        boardMember.getMemberPosts().add(this);
        board.getBoardPosts().add(this);
    }

    public Post() {

    }

    public int getPostLikesCount() {
        return postLikes.size();
    }

    public int getPostCommentsCount() {
        return postComments.size();
    }

    public boolean isLikedByUser(User user) {
        return postLikes.stream()
                .anyMatch(postLike -> postLike.getId().getUserId().equals(user.getUserId()));
    }
}
