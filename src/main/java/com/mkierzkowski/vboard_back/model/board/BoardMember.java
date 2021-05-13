package com.mkierzkowski.vboard_back.model.board;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
import com.mkierzkowski.vboard_back.model.post.Post;
import com.mkierzkowski.vboard_back.model.post.PostComment;
import com.mkierzkowski.vboard_back.model.post.PostLike;
import com.mkierzkowski.vboard_back.model.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardMember extends Auditable<String> {

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

    @OneToMany(mappedBy = "boardMember", orphanRemoval = true)
    List<Post> memberPosts;

    @OneToMany(mappedBy = "boardMember", orphanRemoval = true)
    List<PostLike> memberPostLikes;

    @OneToMany(mappedBy = "boardMember", orphanRemoval = true)
    List<PostComment> memberPostComments;

    Integer orderIndex;

    @NotNull
    Boolean wantNotifications;

    @NotNull
    Boolean isAdmin;

    @NotNull
    Boolean didLeft = false;

    public BoardMember(User user, Board board, Boolean wantNotifications, Boolean isAdmin) {
        this.id = new BoardMemberKey(user.getUserId(), board.getBoardId());

        this.user = user;
        this.board = board;
        this.wantNotifications = wantNotifications;
        this.isAdmin = isAdmin;

        user.getJoinedBoardsForModification().add(this);
        board.getBoardMembers().add(this);
    }

    public BoardMember() {

    }

    public Date getJoinDate() {
        return this.getCreatedDate();
    }

    public int getMemberPostsCount() {
        return memberPosts.size();
    }

    public int getNotificationsCount() {
        int notificationsCount = 0;

        //TODO: count new posts if user wants notifications for posts
        if (isAdmin) {
            notificationsCount += board.getBoardJoinRequests().size();
        }

        return notificationsCount;
    }

}
