package com.mkierzkowski.vboard_back.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mkierzkowski.vboard_back.dto.response.refresh.RefreshResponseDto;
import com.mkierzkowski.vboard_back.exception.EntityType;
import com.mkierzkowski.vboard_back.exception.ExceptionType;
import com.mkierzkowski.vboard_back.exception.VBoardException;
import com.mkierzkowski.vboard_back.model.user.User;
import com.mkierzkowski.vboard_back.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.mkierzkowski.vboard_back.security.SecurityConstants.*;

@Component
public class RefreshTokenService {

    @Autowired
    UserService userService;

    public RefreshResponseDto getNewAccessToken(String refreshToken) {
        try {
            String subject = JWT.require(Algorithm.HMAC512(REFRESH_TOKEN_SECRET.getBytes()))
                    .build()
                    .verify(refreshToken)
                    .getSubject();

            if (subject != null) {
                User user = userService.findUserByEmail(subject);
                RefreshResponseDto refreshResponseDto = new RefreshResponseDto();

                refreshResponseDto.setProfilePicUrl(user.getProfilePicUrl());
                refreshResponseDto.setName(user.getName());
                refreshResponseDto.setAccessToken(JWT.create()
                        .withSubject(subject)
                        .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                        .sign(HMAC512(ACCESS_TOKEN_SECRET.getBytes()))
                );
                return refreshResponseDto;
            }
        } catch (JWTVerificationException ex) {
            throw VBoardException.throwException(EntityType.REFRESH_TOKEN, ExceptionType.INVALID, refreshToken);
        }
        return null;
    }
}
