package com.mkierzkowski.vboard_back.model.post;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
            @JoinColumn(name = "user_id"),
            @JoinColumn(name = "board_id")
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

    public Post(BoardMember boardMember, String postText) {
        this.boardMember = boardMember;
        this.board = boardMember.getBoard();
        this.postText = postText;

        boardMember.getMemberPosts().add(this);
        board.getBoardPosts().add(this);
    }

    public Post() {

    }
}
