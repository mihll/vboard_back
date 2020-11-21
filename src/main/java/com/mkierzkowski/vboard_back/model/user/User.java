package com.mkierzkowski.vboard_back.model.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue
    Long id;

    @NotBlank
    String email;

    @NotBlank
    String password;

    @NotBlank
    String profileImgUrl;

    @NotNull
    boolean enabled;
}
