package com.mkierzkowski.vboard_back.model.board;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Board {

    @Id
    @GeneratedValue
    Long boardId;

    @NotNull
    Boolean isPrivate;

    @NotBlank
    String name;

    String description;

    @CreatedDate
    Date creationDate;

    String addressCity;

    String addressPostCode;

    String addressStreet;

    @OneToMany(mappedBy = "board")
    List<BoardMember> boardMembers = new ArrayList<>();
}
