package com.mkierzkowski.vboard_back.exception;

public enum ExceptionType {
    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate"),
    EMAIL_ERROR("email.error"),
    NOT_VERIFIED("not.verified"),
    EXPIRED("expired"),
    INVALID("invalid"),
    FAILED("operation.failed");

    String value;

    ExceptionType(String value) {
        this.value = value;
    }

    String getValue() {
        return this.value;
    }
}
