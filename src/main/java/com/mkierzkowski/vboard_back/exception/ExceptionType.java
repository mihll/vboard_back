package com.mkierzkowski.vboard_back.exception;

public enum ExceptionType {
    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate"),
    VERIFICATION_EMAIL_ERROR("verification.email.error");

    String value;

    ExceptionType(String value) {
        this.value = value;
    }

    String getValue() {
        return this.value;
    }
}
