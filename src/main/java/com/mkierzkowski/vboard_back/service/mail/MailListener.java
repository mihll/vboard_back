package com.mkierzkowski.vboard_back.service.mail;

import com.mkierzkowski.vboard_back.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MailListener implements ApplicationListener<OnScheduleMailToSendEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Override
    public void onApplicationEvent(OnScheduleMailToSendEvent event) {
        if (event instanceof OnRegistrationCompleteEvent) {
            this.confirmRegistration((OnRegistrationCompleteEvent) event);
        } else if (event instanceof OnResetPasswordEvent) {
            this.sendPasswordResetLink((OnResetPasswordEvent) event);
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getVerificationToken().getUser();
        String token = event.getVerificationToken().getToken();

        String recipientAddress = user.getEmail();
        String subject = "Potwierdzenie rejestracji - VBoard";
        String confirmationUrl = "/confirmSignup?token=" + token;
        String message = "Potwierdź swój adres email, klikając w poniższy link:" + "\r\n" + "http://localhost:4200" + confirmationUrl;

        sendMail(recipientAddress, subject, message);
    }

    private void sendPasswordResetLink(OnResetPasswordEvent event) {
        User user = event.getPasswordResetToken().getUser();
        String token = event.getPasswordResetToken().getToken();

        String recipientAddress = user.getEmail();
        String subject = "Reset hasła - VBoard";
        String resetUrl = "/changePassword?token=" + token;
        String message = "Zresetuj swoje hasło, klikając w poniższy link:" + "\r\n" + "http://localhost:4200" + resetUrl;

        sendMail(recipientAddress, subject, message);
    }

    private void sendMail(String recipientAddress, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(Objects.requireNonNull(env.getProperty("MAIL_USERNAME")));
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
