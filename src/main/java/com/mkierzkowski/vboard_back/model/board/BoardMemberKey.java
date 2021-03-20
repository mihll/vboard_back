package com.mkierzkowski.vboard_back.model.board;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class BoardMemberKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "board_id")
    Long boardId;

    public BoardMemberKey(Long userId, Long boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }

    public BoardMemberKey() { }
}
