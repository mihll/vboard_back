package com.mkierzkowski.vboard_back.model.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "institution_users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "institutionName")
        })
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InstitutionUser extends User {

    @NotBlank
    String institutionName;

    String addressCity;

    String addressPostCode;

    String addressStreet;
}
