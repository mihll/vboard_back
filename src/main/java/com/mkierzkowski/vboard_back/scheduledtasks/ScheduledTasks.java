package com.mkierzkowski.vboard_back.scheduledtasks;

import com.mkierzkowski.vboard_back.model.token.PasswordResetToken;
import com.mkierzkowski.vboard_back.model.token.VerificationToken;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.service.user.UserService;
import com.mkierzkowski.vboard_back.service.user.passwordreset.PasswordResetTokenService;
import com.mkierzkowski.vboard_back.service.user.verification.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    VerificationTokenService verificationTokenService;

    @Autowired
    PasswordResetTokenService passwordResetTokenService;

    @Autowired
    UserService userService;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void deleteExpiredUsers() {
        log.info("Starting expired users removal...");
        Calendar cal = Calendar.getInstance();
        Iterable<VerificationToken> verificationTokens = verificationTokenService.getAllVerificationTokens();
        AtomicInteger removalCounter = new AtomicInteger();
        verificationTokens.forEach((verificationToken -> {
            if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                User user = verificationToken.getUser();
                log.info("EXPIRED: Deleting user - " + user.getEmail());
                removalCounter.getAndIncrement();
                verificationTokenService.deleteVerificationToken(verificationToken);
                userService.deleteUser(user);
            }
        }));
        log.info("Expired users removal finished - removed " + removalCounter + " users");
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void deleteExpiredPasswordResetTokens() {
        log.info("Starting expired password reset tokens removal...");
        Calendar cal = Calendar.getInstance();
        Iterable<PasswordResetToken> passwordResetTokens = passwordResetTokenService.getAllPasswordResetTokens();
        AtomicInteger removalCounter = new AtomicInteger();
        passwordResetTokens.forEach((passwordResetToken -> {
            if ((passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                User user = passwordResetToken.getUser();
                log.info("EXPIRED: Deleting password reset token for - " + user.getEmail());
                removalCounter.getAndIncrement();
                passwordResetTokenService.deletePasswordResetToken(passwordResetToken);
            }
        }));
        log.info("Expired password reset tokens removal finished - removed " + removalCounter + " tokens");
    }
}
