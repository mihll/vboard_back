package com.mkierzkowski.vboard_back.service.user;

import com.mkierzkowski.vboard_back.dto.mapper.user.VerificationMapper;
import com.mkierzkowski.vboard_back.dto.model.user.UserDto;
import com.mkierzkowski.vboard_back.dto.model.user.VerificationDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

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

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return VBoardException.throwException(entityType, exceptionType, args);
    }
}
