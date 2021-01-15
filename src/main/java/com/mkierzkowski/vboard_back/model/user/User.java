package com.mkierzkowski.vboard_back.model.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "all_users_shared",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue
    Long userId;

    @NotBlank
    String email;

    @NotBlank
    String password;

    @NotBlank
    String profileImgUrl;

    @NotNull
    boolean enabled;

    public String getName() {
        if (this instanceof PersonUser)
            return ((PersonUser) this).getFirstName() + " " + ((PersonUser) this).getLastName();
        else return ((InstitutionUser) this).getInstitutionName();
    }

}
