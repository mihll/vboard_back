package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordChangeRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordResetRequestDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.UserRepository;
import com.mkierzkowski.vboard_back.service.user.passwordreset.PasswordResetTokenService;
import com.mkierzkowski.vboard_back.service.user.verification.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    @Transactional
    public User findUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            return user.get();
        }
        throw VBoardException.throwException(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void verifyUserForToken(String token) {
        User verifiedUser = verificationTokenService.getUserForValidVerificationToken(token);
        verifiedUser.setEnabled(true);
        userRepository.save(verifiedUser);
    }

    @Override
    @Transactional
    public void resetPassword(UserPasswordResetRequestDto userPasswordResetRequestDto) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userPasswordResetRequestDto.getEmail()));
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();

            passwordResetTokenService.createPasswordResetToken(user.get(), token);

            String recipientAddress = user.get().getEmail();
            String subject = "Reset hasła - VBoard";
            String resetUrl = "/changePassword?token=" + token;
            String message = "Zresetuj swoje hasło, klikając w poniższy link:";

            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(Objects.requireNonNull(env.getProperty("MAIL_USERNAME")));
            email.setTo(recipientAddress);
            email.setSubject(subject);
            email.setText(message + "\r\n" + "http://localhost:4200" + resetUrl);
            mailSender.send(email);
        }
        // uncomment if you would like to inform client that user does not exist
        /*else {
            throw VBoardException.throwException(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, userPasswordResetRequestDto.getEmail());
        }*/
    }

    @Override
    @Transactional
    public void changePassword(UserPasswordChangeRequestDto userPasswordChangeRequestDto, String token) {
        User user = passwordResetTokenService.getUserForValidPasswordResetToken(token);
        user.setPassword(bCryptPasswordEncoder.encode(userPasswordChangeRequestDto.getPassword()));
        userRepository.save(user);
    }
}
