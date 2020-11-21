package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.mapper.user.InstitutionUserMapper;
import com.mkierzkowski.vboard_back.dto.model.user.RegisterInstitutionUserDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.InstitutionUser;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.InstitutionUserRepository;
import com.mkierzkowski.vboard_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InstitutionUserServiceImpl implements InstitutionUserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstitutionUserRepository institutionUserRepository;

    @Override
    public RegisterInstitutionUserDto signup(RegisterInstitutionUserDto registerInstitutionUserDto) {
        User user = userRepository.findByEmail(registerInstitutionUserDto.getEmail());
        if (user == null) {
            InstitutionUser institutionUser = (InstitutionUser) new InstitutionUser()
                    .setInstitutionName(registerInstitutionUserDto.getInstitutionName())
                    .setProfileImgUrl("testProfilePicInstURL")
                    .setEmail(registerInstitutionUserDto.getEmail())
                    .setPassword(bCryptPasswordEncoder.encode(registerInstitutionUserDto.getPassword()))
                    .setEnabled(false);
            return InstitutionUserMapper.toRegisterInstitutionUserDto(institutionUserRepository.save(institutionUser));
        }
        throw exception(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, registerInstitutionUserDto.getEmail());
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return VBoardException.throwException(entityType, exceptionType, args);
    }
}
