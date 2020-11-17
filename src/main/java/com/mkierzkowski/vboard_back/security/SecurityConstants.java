package com.mkierzkowski.vboard_back.security;

public interface SecurityConstants {
    String SECRET = "v9y$B&E)H@McQfTjWnZr4u7x!z%C*F-JaNdRgUkXp2s5v8y/B?D(G+KbPeShVmYq";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    long EXPIRATION_TIME = 864_000_000; // 10 days
}
