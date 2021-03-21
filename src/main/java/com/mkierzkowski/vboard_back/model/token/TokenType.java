package com.mkierzkowski.vboard_back.model.token;

public enum TokenType {
    PASSWORD_RESET(60 * 24),
    VERIFICATION(60 * 24);

    private final int expirationTimeInMinutes;

    TokenType(int expirationTimeInMinutes) {
        this.expirationTimeInMinutes = expirationTimeInMinutes;
    }

    int getExpirationTimeInMinutes() {
        return expirationTimeInMinutes;
    }
}
