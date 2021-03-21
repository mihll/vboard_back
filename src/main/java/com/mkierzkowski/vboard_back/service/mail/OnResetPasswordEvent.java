package com.mkierzkowski.vboard_back.service.mail;

import com.mkierzkowski.vboard_back.model.token.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnResetPasswordEvent extends OnScheduleMailToSendEvent {
    private Token passwordResetToken;

    public OnResetPasswordEvent(Token passwordResetToken) {
        super(passwordResetToken);
        this.passwordResetToken = passwordResetToken;
    }
}
