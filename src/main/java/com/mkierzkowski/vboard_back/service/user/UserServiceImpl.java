package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.mapper.user.UserPasswordChangedMapper;
import com.mkierzkowski.vboard_back.dto.mapper.user.VerificationMapper;
import com.mkierzkowski.vboard_back.dto.model.user.*;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.UserRepository;
import org.modelmapper.ModelMapper;
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
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Transactional
    public UserDto findUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDto.class);
        }
        throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public VerificationDto verifyRegisteredUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
        return VerificationMapper.toVerificationDto(user);
    }

    @Override
    public UserPasswordResetDto resetPassword(UserPasswordResetDto userPasswordResetDto) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userPasswordResetDto.getEmail()));
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();

            UserPasswordResetDto responseDto = passwordResetTokenService.createPasswordResetToken(user.get(), token);

            String recipientAddress = user.get().getEmail();
            String subject = "Reset hasła - VBoard";
            String resetUrl = "/user/changePassword?token=" + token;
            String message = "Zresetuj swoje hasło, klikając w poniższy link:";

            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(Objects.requireNonNull(env.getProperty("MAIL_USERNAME")));
            email.setTo(recipientAddress);
            email.setSubject(subject);
            email.setText(message + "\r\n" + "http://localhost:8080" + resetUrl);
            mailSender.send(email);

            return responseDto;
        }
        throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, userPasswordResetDto.getEmail());
    }

    @Override
    @Transactional
    public UserPasswordChangedDto changePassword(UserPasswordChangeDto userPasswordChangeDto) {
        User user = passwordResetTokenService.getUserForValidPasswordResetToken(userPasswordChangeDto.getToken());
        user.setPassword(bCryptPasswordEncoder.encode(userPasswordChangeDto.getNewPassword()));
        userRepository.save(user);
        return UserPasswordChangedMapper.toUserPasswordChangedDto(user);
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return VBoardException.throwException(entityType, exceptionType, args);
    }
}
