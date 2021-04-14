package com.mkierzkowski.vboard_back.model.board;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
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

    String description;

    String addressCity;

    String addressPostCode;

    String addressStreet;

    @OneToMany(mappedBy = "board")
    List<BoardMember> boardMembers = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    List<BoardJoinRequest> boardJoinRequests = new ArrayList<>();
}
