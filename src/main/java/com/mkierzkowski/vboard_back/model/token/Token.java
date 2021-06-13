package com.mkierzkowski.vboard_back.model.token;

import com.mkierzkowski.vboard_back.config.auditing.Auditable;
import com.mkierzkowski.vboard_back.model.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token extends Auditable<String> {

    @Id
    @GeneratedValue
    Long tokenId;

    String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    Date expiryDate;

    TokenType type;

    public Token(String token, User user, TokenType type) {
        super();
        this.token = token;
        this.user = user;
        this.type = type;
        this.expiryDate = calculateExpiryDate();

        user.getGeneratedTokens().add(this);
    }

    Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, this.type.getExpirationTimeInMinutes());
        return new Date(cal.getTime().getTime());
    }
}
