package com.mkierzkowski.vboard_back.model.user;

import com.mkierzkowski.vboard_back.model.board.BoardMember;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "all_users_shared",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

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

    @OneToMany(mappedBy = "user")
    List<BoardMember> joinedBoards = new ArrayList<>();

    public String getName() {
        if (this instanceof PersonUser)
            return ((PersonUser) this).getFirstName() + " " + ((PersonUser) this).getLastName();
        else return ((InstitutionUser) this).getInstitutionName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
