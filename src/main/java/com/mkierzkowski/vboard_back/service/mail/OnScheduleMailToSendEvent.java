package com.mkierzkowski.vboard_back.service.mail;

import org.springframework.context.ApplicationEvent;

public abstract class OnScheduleMailToSendEvent extends ApplicationEvent {
    public OnScheduleMailToSendEvent(Object source) {
        super(source);
    }
}
