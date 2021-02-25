package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.request.signup.InstitutionUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.signup.PersonUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.user.InstitutionUserUpdateRequestDto;
import com.mkierzkowski.vboard_back.dto.request.user.PersonUserUpdateRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordChangeRequestDto;
import com.mkierzkowski.vboard_back.dto.request.userpassword.UserPasswordResetRequestDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.InstitutionUser;
import com.mkierzkowski.vboard_back.model.user.PersonUser;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.InstitutionUserRepository;
import com.mkierzkowski.vboard_back.repository.PersonUserRepository;
import com.mkierzkowski.vboard_back.repository.UserRepository;
import com.mkierzkowski.vboard_back.service.registrationmail.OnRegistrationCompleteEvent;
import com.mkierzkowski.vboard_back.service.user.passwordreset.PasswordResetTokenService;
import com.mkierzkowski.vboard_back.service.user.verification.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Environment env;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonUserRepository personUserRepository;

    @Autowired
    private InstitutionUserRepository institutionUserRepository;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    @Transactional
    public void signup(PersonUserSignupRequestDto personUserSignupRequestDto) {
        User user = userRepository.findByEmail(personUserSignupRequestDto.getEmail());
        if (user == null) {
            PersonUser personUser = (PersonUser) new PersonUser()
                    .setFirstName(personUserSignupRequestDto.getFirstName())
                    .setLastName(personUserSignupRequestDto.getLastName())
                    .setProfileImgUrl("testProfilePicURL")
                    .setEmail(personUserSignupRequestDto.getEmail())
                    .setPassword(bCryptPasswordEncoder.encode(personUserSignupRequestDto.getPassword()))
                    .setEnabled(false);
            PersonUser registeredPersonUser = personUserRepository.save(personUser);
            try {
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredPersonUser));
            } catch (RuntimeException ex) {
                throw VBoardException.throwException(EntityType.USER, ExceptionType.VERIFICATION_EMAIL_ERROR);
            }
        } else {
            throw VBoardException.throwException(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, personUserSignupRequestDto.getEmail());
        }
    }

    @Override
    @Transactional
    public void signup(InstitutionUserSignupRequestDto institutionUserSignupRequestDto) {
        User user = userRepository.findByEmail(institutionUserSignupRequestDto.getEmail());
        if (user == null) {
            InstitutionUser institutionUser = (InstitutionUser) new InstitutionUser()
                    .setInstitutionName(institutionUserSignupRequestDto.getInstitutionName())
                    .setProfileImgUrl("testProfilePicInstURL")
                    .setEmail(institutionUserSignupRequestDto.getEmail())
                    .setPassword(bCryptPasswordEncoder.encode(institutionUserSignupRequestDto.getPassword()))
                    .setEnabled(false);
            InstitutionUser registeredInstitutionUser = institutionUserRepository.save(institutionUser);
            try {
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredInstitutionUser));
            } catch (RuntimeException ex) {
                throw VBoardException.throwException(EntityType.USER, ExceptionType.VERIFICATION_EMAIL_ERROR);
            }
        } else {
            throw VBoardException.throwException(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, institutionUserSignupRequestDto.getEmail());
        }
    }

    @Override
    public PersonUser update(PersonUserUpdateRequestDto personUserUpdateRequestDto) {
        User currentUser = getCurrentUser();
        if (!(currentUser instanceof PersonUser)) {
            throw VBoardException.throwException(EntityType.USER, ExceptionType.INVALID, personUserUpdateRequestDto.toString());
        }
        PersonUser currentPersonUser = (PersonUser) currentUser;
        currentPersonUser.setFirstName(personUserUpdateRequestDto.getFirstName());
        currentPersonUser.setLastName(personUserUpdateRequestDto.getLastName());
        currentPersonUser.setBirthDate(personUserUpdateRequestDto.getBirthDate());

        return userRepository.saveAndFlush(currentPersonUser);
    }

    @Override
    public InstitutionUser update(InstitutionUserUpdateRequestDto institutionUserUpdateRequestDto) {
        User currentUser = getCurrentUser();
        if (!(currentUser instanceof InstitutionUser)) {
            throw VBoardException.throwException(EntityType.USER, ExceptionType.INVALID, institutionUserUpdateRequestDto.toString());
        }
        InstitutionUser currentInstitutionUser = (InstitutionUser) currentUser;
        currentInstitutionUser.setInstitutionName(institutionUserUpdateRequestDto.getInstitutionName());
        currentInstitutionUser.setAddressCity(institutionUserUpdateRequestDto.getAddressCity());
        currentInstitutionUser.setAddressPostCode(institutionUserUpdateRequestDto.getAddressPostCode());
        currentInstitutionUser.setAddressStreet(institutionUserUpdateRequestDto.getAddressStreet());

        return userRepository.saveAndFlush(currentInstitutionUser);
    }

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
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw VBoardException.throwException(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, id.toString());
    }

    @Override
    public User getCurrentUser() {
        User userFormContext = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findUserById(userFormContext.getUserId());
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public User saveAndFlushUser(User user) {
        return userRepository.saveAndFlush(user);
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
