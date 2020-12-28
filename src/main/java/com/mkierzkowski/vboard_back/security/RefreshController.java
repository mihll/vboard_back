package com.mkierzkowski.vboard_back.security;

import com.mkierzkowski.vboard_back.dto.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Response refreshUserToken(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        String newAccessToken = refreshTokenService.getNewAccessToken(refreshToken);

        response.addHeader(HEADER_STRING, ACCESS_TOKEN_PREFIX + newAccessToken);
        return Response.ok();
    }
}
