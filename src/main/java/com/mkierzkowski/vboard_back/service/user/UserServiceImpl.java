package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.request.signup.AbstractUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.signup.InstitutionUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.signup.PersonUserSignupRequestDto;
import com.mkierzkowski.vboard_back.dto.request.user.AbstractUserUpdateRequestDto;
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
import com.mkierzkowski.vboard_back.service.storage.StorageService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.mkierzkowski.vboard_back.service.storage.RelativePaths.PROFILE_PICS;

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

    @Autowired
    private StorageService storageService;

    @Override
    public void signup(AbstractUserSignupRequestDto userSignupRequestDto) {
        User user = userRepository.findByEmail(userSignupRequestDto.getEmail());
        User registeredUser;
        if (user == null) {
            if (userSignupRequestDto instanceof PersonUserSignupRequestDto) {
                PersonUserSignupRequestDto personUserSignupRequestDto = (PersonUserSignupRequestDto) userSignupRequestDto;
                PersonUser personUser = (PersonUser) new PersonUser()
                        .setFirstName(personUserSignupRequestDto.getFirstName())
                        .setLastName(personUserSignupRequestDto.getLastName())
                        .setProfilePicFilename("defaultPersonProfilePic.jpg")
                        .setEmail(personUserSignupRequestDto.getEmail())
                        .setPassword(bCryptPasswordEncoder.encode(personUserSignupRequestDto.getPassword()))
                        .setEnabled(false);
                registeredUser = personUserRepository.save(personUser);
            } else if (userSignupRequestDto instanceof InstitutionUserSignupRequestDto) {
                InstitutionUserSignupRequestDto institutionUserSignupRequestDto = (InstitutionUserSignupRequestDto) userSignupRequestDto;
                InstitutionUser institutionUser = (InstitutionUser) new InstitutionUser()
                        .setInstitutionName(institutionUserSignupRequestDto.getInstitutionName())
                        .setProfilePicFilename("defaultInstitutionProfilePic.jpg")
                        .setEmail(institutionUserSignupRequestDto.getEmail())
                        .setPassword(bCryptPasswordEncoder.encode(institutionUserSignupRequestDto.getPassword()))
                        .setEnabled(false);
                registeredUser = institutionUserRepository.save(institutionUser);
            } else {
                throw VBoardException.throwException(EntityType.REQUEST, ExceptionType.INVALID);
            }

            try {
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser));
            } catch (RuntimeException ex) {
                throw VBoardException.throwException(EntityType.USER, ExceptionType.VERIFICATION_EMAIL_ERROR);
            }

        } else {
            throw VBoardException.throwException(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, userSignupRequestDto.getEmail());
        }
    }

    @Override
    public User update(AbstractUserUpdateRequestDto userUpdateRequestDto) {
        User currentUser = getCurrentUser();
        if (currentUser instanceof PersonUser && userUpdateRequestDto instanceof PersonUserUpdateRequestDto) {
            PersonUser currentPersonUser = (PersonUser) currentUser;
            PersonUserUpdateRequestDto personUserUpdateRequestDto = (PersonUserUpdateRequestDto) userUpdateRequestDto;
            currentPersonUser.setFirstName(personUserUpdateRequestDto.getFirstName());
            currentPersonUser.setLastName(personUserUpdateRequestDto.getLastName());
            currentPersonUser.setBirthDate(personUserUpdateRequestDto.getBirthDate());

            return userRepository.saveAndFlush(currentPersonUser);
        } else if (currentUser instanceof InstitutionUser && userUpdateRequestDto instanceof InstitutionUserUpdateRequestDto) {
            InstitutionUser currentInstitutionUser = (InstitutionUser) currentUser;
            InstitutionUserUpdateRequestDto institutionUserUpdateRequestDto = (InstitutionUserUpdateRequestDto) userUpdateRequestDto;
            currentInstitutionUser.setInstitutionName(institutionUserUpdateRequestDto.getInstitutionName());
            currentInstitutionUser.setAddressCity(institutionUserUpdateRequestDto.getAddressCity());
            currentInstitutionUser.setAddressPostCode(institutionUserUpdateRequestDto.getAddressPostCode());
            currentInstitutionUser.setAddressStreet(institutionUserUpdateRequestDto.getAddressStreet());

            return userRepository.saveAndFlush(currentInstitutionUser);
        } else {
            throw VBoardException.throwException(EntityType.REQUEST, ExceptionType.INVALID);
        }
    }

    @Override
    public User changeProfilePic(MultipartFile profilePic) {

        // if profilePic is empty, user wants to reset profile picture to default
        if (profilePic.isEmpty()) {
            User currentUser = getCurrentUser();
            if (currentUser.getProfilePicFilename().startsWith("default")) {
                throw VBoardException.throwException(EntityType.PROFILE_PIC, ExceptionType.INVALID, "You profile picture is already the default one");
            }
            storageService.delete(currentUser.getProfilePicFilename(), PROFILE_PICS.getPath());
            if (currentUser instanceof PersonUser) {
                currentUser.setProfilePicFilename("defaultPersonProfilePic.jpg");
            } else if (currentUser instanceof InstitutionUser) {
                currentUser.setProfilePicFilename("defaultInstitutionProfilePic.jpg");
            }
            return userRepository.save(currentUser);
        }

        if (!Objects.equals(profilePic.getContentType(), "image/jpeg")) {
            throw VBoardException.throwException(EntityType.PROFILE_PIC, ExceptionType.INVALID, "File is in invalid format (only JPEGs are allowed)");
        }

        try {
            BufferedImage image = ImageIO.read(profilePic.getInputStream());
            if (image != null) {
                if (image.getWidth() != 200 || image.getHeight() != 200) {
                    throw VBoardException.throwException(EntityType.PROFILE_PIC, ExceptionType.INVALID, "File is in wrong size (should be 200x200 pixels)");
                }
            } else {
                throw VBoardException.throwException(EntityType.PROFILE_PIC, ExceptionType.INVALID, "File is not an image");
            }

        } catch (IOException e) {
            throw VBoardException.throwException(EntityType.PROFILE_PIC, ExceptionType.INVALID, "File is empty");
        }

        String filename = storageService.store(profilePic, UUID.randomUUID().toString() + ".jpg", PROFILE_PICS.getPath());

        User currentUser = getCurrentUser();
        currentUser.setProfilePicFilename(filename);
        return userRepository.save(currentUser);
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
    public void changePassword(UserPasswordChangeRequestDto userPasswordChangeRequestDto) {
        User currentUser;
        if (userPasswordChangeRequestDto.getToken() != null && !userPasswordChangeRequestDto.getToken().isEmpty()) {
            currentUser = passwordResetTokenService.getUserForValidPasswordResetToken(userPasswordChangeRequestDto.getToken());
            currentUser.setPassword(bCryptPasswordEncoder.encode(userPasswordChangeRequestDto.getNewPassword()));
        } else if (userPasswordChangeRequestDto.getCurrentPassword() != null && !userPasswordChangeRequestDto.getCurrentPassword().isEmpty()) {
            currentUser = getCurrentUser();
            if (bCryptPasswordEncoder.matches(userPasswordChangeRequestDto.getCurrentPassword(), currentUser.getPassword())) {
                currentUser.setPassword(bCryptPasswordEncoder.encode(userPasswordChangeRequestDto.getNewPassword()));
            } else {
                throw VBoardException.throwException(EntityType.CURRENT_PASSWORD, ExceptionType.INVALID);
            }
        } else {
            throw VBoardException.throwException(EntityType.REQUEST, ExceptionType.INVALID);
        }
        userRepository.save(currentUser);
    }
}
