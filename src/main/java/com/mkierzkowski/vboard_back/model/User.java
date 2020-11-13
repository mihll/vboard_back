package com.mkierzkowski.vboard_back.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
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
}
