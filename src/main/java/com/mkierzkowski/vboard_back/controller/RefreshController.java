package com.mkierzkowski.vboard_back.controller;

import com.mkierzkowski.vboard_back.dto.response.refresh.RefreshResponseDto;
import com.mkierzkowski.vboard_back.security.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static com.mkierzkowski.vboard_back.security.SecurityConstants.ACCESS_TOKEN_PREFIX;
import static com.mkierzkowski.vboard_back.security.SecurityConstants.HEADER_STRING;

@RestController
@RequestMapping("/refresh")
public class RefreshController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @SuppressWarnings("rawtypes")
    @PostMapping
    public ResponseEntity refreshUserToken(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }
        RefreshResponseDto refreshResponseDto = refreshTokenService.getNewAccessToken(refreshToken);

        response.addHeader(HEADER_STRING, ACCESS_TOKEN_PREFIX + refreshResponseDto.getAccessToken());
        return ResponseEntity.ok(refreshResponseDto);
    }
}
