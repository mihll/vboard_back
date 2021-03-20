package com.mkierzkowski.vboard_back.service.mail;

import com.mkierzkowski.vboard_back.model.token.PasswordResetToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnResetPasswordEvent extends OnScheduleMailToSendEvent {
    private PasswordResetToken passwordResetToken;

    public OnResetPasswordEvent(PasswordResetToken passwordResetToken) {
        super(passwordResetToken);
        this.passwordResetToken = passwordResetToken;
    }
}
