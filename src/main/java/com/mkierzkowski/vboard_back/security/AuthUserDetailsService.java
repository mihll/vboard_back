package com.mkierzkowski.vboard_back.security;

import com.mkierzkowski.vboard_back.dto.model.user.UserDto;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto userDto = userService.findUserByEmail(email);
        if (userDto != null) {
            return buildUserForAuthentication(userDto);
        } else {
            throw new UsernameNotFoundException("user with email " + email + " does not exist.");
        }
    }

    private UserDetails buildUserForAuthentication(UserDto user) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
