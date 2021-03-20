package com.mkierzkowski.vboard_back.service.mail;

import com.mkierzkowski.vboard_back.model.token.VerificationToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends OnScheduleMailToSendEvent {
    private VerificationToken verificationToken;

    public OnRegistrationCompleteEvent(VerificationToken verificationToken) {
        super(verificationToken);
        this.verificationToken = verificationToken;
    }
}
