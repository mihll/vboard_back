package com.mkierzkowski.vboard_back.security;

import com.mkierzkowski.vboard_back.dto.model.user.UserDto;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            UserDto userDto = modelMapper.map(user.get(), UserDto.class);
            if (userDto.getEnabled()) {
                return buildUserForAuthentication(userDto);
            } else {
                throw new UsernameNotFoundException("user with email " + email + " is not verified.");
            }
        } else {
            throw new UsernameNotFoundException("user with email " + email + " does not exist.");
        }
    }

    private UserDetails buildUserForAuthentication(UserDto user) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
