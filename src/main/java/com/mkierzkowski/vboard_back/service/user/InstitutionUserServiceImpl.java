package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.request.signup.InstitutionUserSignupRequestDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.InstitutionUser;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.InstitutionUserRepository;
import com.mkierzkowski.vboard_back.repository.UserRepository;
import com.mkierzkowski.vboard_back.service.registrationmail.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class InstitutionUserServiceImpl implements InstitutionUserService {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstitutionUserRepository institutionUserRepository;

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
}
