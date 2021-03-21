package com.mkierzkowski.vboard_back.service.mail;

import com.mkierzkowski.vboard_back.model.token.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends OnScheduleMailToSendEvent {
    private Token verificationToken;

    public OnRegistrationCompleteEvent(Token verificationToken) {
        super(verificationToken);
        this.verificationToken = verificationToken;
    }
}
