package com.mkierzkowski.vboard_back.model.user;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users_shared",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditable<String> implements UserDetails {

    @Id
    @GeneratedValue
    Long userId;

    @NotBlank
    String email;

    @NotBlank
    String password;

    @NotBlank
    String profilePicFilename;

    @NotNull
    boolean enabled;

    @OneToMany(mappedBy = "user")
    @OrderColumn(name = "orderIndex")
    List<BoardMember> joinedBoards = new ArrayList<>();

    public String getName() {
        if (this instanceof PersonUser)
            return ((PersonUser) this).getFirstName() + " " + ((PersonUser) this).getLastName();
        else return ((InstitutionUser) this).getInstitutionName();
    }

    public String getProfilePicUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/resource/profilePic/")
                .path(this.getProfilePicFilename())
                .toUriString();
    }

    public Date getSignupDate() {
        return this.getCreatedDate();
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
