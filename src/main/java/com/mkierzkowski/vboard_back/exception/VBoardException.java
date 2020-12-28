package com.mkierzkowski.vboard_back.exception;

import com.mkierzkowski.vboard_back.config.CustomPropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class VBoardException {

    private static CustomPropertiesConfig customPropertiesConfig;

    @Autowired
    public VBoardException(CustomPropertiesConfig customPropertiesConfig) {
        VBoardException.customPropertiesConfig = customPropertiesConfig;
    }

    public static RuntimeException throwException(EntityType entityType, ExceptionType exceptionType, String... args) {
        String messageTemplate = getMessageTemplate(entityType, exceptionType);
        return throwException(exceptionType, messageTemplate, args);
    }

    private static String getMessageTemplate(EntityType entityType, ExceptionType exceptionType) {
        return entityType.name().concat(".").concat(exceptionType.getValue()).toLowerCase();
    }

    private static RuntimeException throwException(ExceptionType exceptionType, String messageTemplate, String... args) {
        return switch (exceptionType) {
            case ENTITY_NOT_FOUND -> new EntityNotFoundException(format(messageTemplate, args));
            case DUPLICATE_ENTITY -> new DuplicateEntityException(format(messageTemplate, args));
            case VERIFICATION_EMAIL_ERROR -> new VerificationEmailException(format(messageTemplate, args));
            case EXPIRED -> new ExpiredVerificationTokenException(format(messageTemplate, args));
            case NOT_VERIFIED -> new NotVerifiedException(format(messageTemplate, args));
            case INVALID -> new InvalidTokenException(format(messageTemplate, args));
        };
    }

    private static String format(String template, String... args) {
        Optional<String> templateContent = Optional.ofNullable(customPropertiesConfig.getConfigValue(template));
        return templateContent.map(s -> MessageFormat.format(s, (Object[]) args)).orElseGet(() -> String.format(template, (Object[]) args));
    }

    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static class DuplicateEntityException extends RuntimeException {
        public DuplicateEntityException(String message) {
            super(message);
        }
    }

    public static class VerificationEmailException extends RuntimeException {
        public VerificationEmailException(String message) {
            super(message);
        }
    }

    public static class ExpiredVerificationTokenException extends RuntimeException {
        public ExpiredVerificationTokenException(String message) {
            super(message);
        }
    }

    public static class NotVerifiedException extends RuntimeException {
        public NotVerifiedException(String message) {
            super(message);
        }
    }

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }
    }
}
