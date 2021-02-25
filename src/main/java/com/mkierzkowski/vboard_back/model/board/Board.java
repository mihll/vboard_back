package com.mkierzkowski.vboard_back.model.board;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
//TODO: Implement Auditable everywhere needed
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue
    Long boardId;

    @NotNull
    Boolean isPrivate;

    @NotBlank
    String boardName;

    String description;

    @CreatedDate
    Date creationDate;

    String addressCity;

    String addressPostCode;

    String addressStreet;

    @OneToMany(mappedBy = "board")
    List<BoardMember> boardMembers = new ArrayList<>();
}
