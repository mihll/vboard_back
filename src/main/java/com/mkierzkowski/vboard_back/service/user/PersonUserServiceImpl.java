package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.request.signup.PersonUserSignupRequestDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.PersonUser;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.PersonUserRepository;
import com.mkierzkowski.vboard_back.repository.UserRepository;
import com.mkierzkowski.vboard_back.service.registrationmail.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PersonUserServiceImpl implements PersonUserService {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonUserRepository personUserRepository;

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
}
