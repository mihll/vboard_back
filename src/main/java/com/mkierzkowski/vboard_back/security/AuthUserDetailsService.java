package com.mkierzkowski.vboard_back.security;

import com.mkierzkowski.vboard_back.model.token.VerificationToken;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.repository.UserRepository;
import com.mkierzkowski.vboard_back.service.user.verification.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            Optional<VerificationToken> verificationToken = Optional.ofNullable(verificationTokenService.getVerificationTokenForUser(user.get()));
            if (user.get().isEnabled()) {
                return user.get();
                //TODO Catch following exceptions
            } else if (verificationToken.isPresent()) {
                throw new DisabledException("User is not verified");
            } else {
                throw new LockedException("User is blocked");
            }
        } else {
            throw new UsernameNotFoundException("user with email " + email + " does not exist.");
        }
    }
}
