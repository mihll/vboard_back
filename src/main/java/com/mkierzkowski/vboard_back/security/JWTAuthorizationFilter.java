package com.mkierzkowski.vboard_back.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mkierzkowski.vboard_back.model.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.mkierzkowski.vboard_back.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final AuthUserDetailsService authUserDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AuthUserDetailsService authUserDetailsService) {
        super(authenticationManager);
        this.authUserDetailsService = authUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(ACCESS_TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        try {
            String userEmail = JWT.require(Algorithm.HMAC512(ACCESS_TOKEN_SECRET.getBytes()))
                    .build()
                    .verify(token.replace(ACCESS_TOKEN_PREFIX, ""))
                    .getSubject();

            if (userEmail != null) {
                User user = (User) authUserDetailsService.loadUserByUsername(userEmail);
                return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            }
            return null;
        } catch (JWTVerificationException ex) {
            return null;
        }
    }
}
