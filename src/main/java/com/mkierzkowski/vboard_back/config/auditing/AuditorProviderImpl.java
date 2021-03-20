package com.mkierzkowski.vboard_back.config.auditing;

import com.mkierzkowski.vboard_back.model.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Optional;

public class AuditorProviderImpl implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            return Optional.of(((WebAuthenticationDetails) auth.getDetails()).getRemoteAddress());
        }
        return Optional.of(((User)auth.getPrincipal()).getUserId().toString());
    }
}
