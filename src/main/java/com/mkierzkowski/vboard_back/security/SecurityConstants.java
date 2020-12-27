package com.mkierzkowski.vboard_back.security;

public interface SecurityConstants {
    String ACCESS_TOKEN_SECRET = "fUjWnZr4u7x!A%D*G-KaPdSgVkYp2s5v8y/B?E(H+MbQeThWmZq4t6w9z$C&F)J@";
    String ACCESS_TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    long ACCESS_TOKEN_EXPIRATION_TIME = 900_000; // 15 minutes

    String REFRESH_TOKEN_SECRET = "aPdSgUkXp2s5v8y/B?E(H+MbQeThWmYq3t6w9z$C&F)J@NcRfUjXn2r4u7x!A%D*";
    long REFRESH_TOKEN_EXPIRATION_TIME = 1_209_600_000; // 14 days
}
