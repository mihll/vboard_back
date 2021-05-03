package com.mkierzkowski.vboard_back.model.board;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
import com.mkierzkowski.vboard_back.model.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardJoinRequest extends Auditable<String> {

    @EmbeddedId
    BoardMemberKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;

    @ManyToOne
    @MapsId("boardId")
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    Board board;

    public BoardJoinRequest(User user, Board board) {
        this.id = new BoardMemberKey(user.getUserId(), board.getBoardId());

        this.user = user;
        this.board = board;

        user.getRequestedBoards().add(this);
        board.getBoardJoinRequests().add(this);
    }

    public BoardJoinRequest() {

    }

    public Date getRequestDate() {
        return this.getCreatedDate();
    }
}
