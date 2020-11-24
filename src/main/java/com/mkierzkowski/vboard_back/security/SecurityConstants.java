package com.mkierzkowski.vboard_back.security;

public interface SecurityConstants {
    String SECRET = "fUjWnZr4u7x!A%D*G-KaPdSgVkYp2s5v8y/B?E(H+MbQeThWmZq4t6w9z$C&F)J@";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    long EXPIRATION_TIME = 864_000_000; // 10 days
}
