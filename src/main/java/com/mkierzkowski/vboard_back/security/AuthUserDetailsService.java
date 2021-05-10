package com.mkierzkowski.vboard_back.security;

import com.mkierzkowski.vboard_back.model.token.Token;
import com.mkierzkowski.vboard_back.model.token.TokenType;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.user.UserRepository;
import com.mkierzkowski.vboard_back.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User (" + email + ") does not exist."));

        Optional<Token> verificationToken = tokenService.getTokenForUserOfType(user, TokenType.VERIFICATION);

        if (user.isEnabled()) {
            return user;
        } else if (verificationToken.isPresent()) {
            throw new DisabledException("User ( " + email + ") is not verified");
        } else {
            throw new LockedException("User (" + email + ") is blocked");
        }

    }
}
