package com.mkierzkowski.vboard_back.model.board;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
import com.mkierzkowski.vboard_back.model.post.Post;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Board extends Auditable<String> {

    @Id
    @GeneratedValue
    Long boardId;

    @NotNull
    Boolean isPrivate;

    @NotNull
    Boolean acceptAll = false;

    @NotBlank
    String boardName;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    String description;

    String addressCity;

    String addressPostCode;

    String addressStreet;

    @OneToMany(mappedBy = "board")
    List<BoardMember> boardMembers = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    List<BoardJoinRequest> boardJoinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    List<Post> boardPosts;

    public Stream<BoardMember> getAdmins() {
        return boardMembers.stream()
                .filter(BoardMember::getIsAdmin);
    }
}
