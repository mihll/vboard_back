package com.mkierzkowski.vboard_back.model.board;

import com.mkierzkowski.vboard_back.model.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardMember {

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

    Integer orderIndex;

    @NotNull
    Boolean wantNotifications;

    @CreatedDate
    Date joinDate;

    @NotNull
    Boolean isAdmin;

    public BoardMember(User user, Board board, Boolean wantNotifications, Boolean isAdmin) {
        this.id = new BoardMemberKey(user.getUserId(), board.getBoardId());

        this.user = user;
        this.board = board;
        this.wantNotifications = wantNotifications;
        this.isAdmin = isAdmin;

        user.getJoinedBoards().add(this);
        board.getBoardMembers().add(this);
    }

    public BoardMember() {

    }
}
