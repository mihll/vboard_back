package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.mapper.user.PersonUserMapper;
import com.mkierzkowski.vboard_back.dto.model.user.RegisterPersonUserDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.PersonUser;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.PersonUserRepository;
import com.mkierzkowski.vboard_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PersonUserServiceImpl implements PersonUserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonUserRepository personUserRepository;

    @Override
    public RegisterPersonUserDto signup(RegisterPersonUserDto registerPersonUserDto) {
        User user = userRepository.findByEmail(registerPersonUserDto.getEmail());
        if (user == null) {
            PersonUser personUser = (PersonUser) new PersonUser()
                    .setFirstName(registerPersonUserDto.getFirstName())
                    .setLastName(registerPersonUserDto.getLastName())
                    .setProfileImgUrl("testProfilePicURL")
                    .setEmail(registerPersonUserDto.getEmail())
                    .setPassword(bCryptPasswordEncoder.encode(registerPersonUserDto.getPassword()));
            return PersonUserMapper.toRegisterPersonUserDto(personUserRepository.save(personUser));
        }
        throw exception(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, registerPersonUserDto.getEmail());
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return VBoardException.throwException(entityType, exceptionType, args);
    }
}
