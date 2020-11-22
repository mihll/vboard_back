package com.mkierzkowski.vboard_back.exception;

import com.mkierzkowski.vboard_back.config.PropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class VBoardException {

    private static PropertiesConfig propertiesConfig;

    @Autowired
    public VBoardException(PropertiesConfig propertiesConfig) {
        VBoardException.propertiesConfig = propertiesConfig;
    }

    public static RuntimeException throwException(EntityType entityType, ExceptionType exceptionType, String... args) {
        String messageTemplate = getMessageTemplate(entityType, exceptionType);
        return throwException(exceptionType, messageTemplate, args);
    }

    private static RuntimeException throwException(ExceptionType exceptionType, String messageTemplate, String... args) {
        if (ExceptionType.ENTITY_NOT_FOUND.equals(exceptionType)) {
            return new EntityNotFoundException(format(messageTemplate, args));
        } else if (ExceptionType.DUPLICATE_ENTITY.equals(exceptionType)) {
            return new DuplicateEntityException(format(messageTemplate, args));
        } else if (ExceptionType.VERIFICATION_EMAIL_ERROR.equals(exceptionType)) {
            return new VerificationEmailException(format(messageTemplate, args));
        } else if (ExceptionType.EXPIRED.equals(exceptionType)) {
            return new ExpiredVerificationTokenException(format(messageTemplate, args));
        } else if (ExceptionType.NOT_VERIFIED.equals(exceptionType)) {
            return new NotVerifiedException(format(messageTemplate, args));
        }
        return new RuntimeException(format(messageTemplate, args));
    }

    private static String getMessageTemplate(EntityType entityType, ExceptionType exceptionType) {
        return entityType.name().concat(".").concat(exceptionType.getValue()).toLowerCase();
    }

    private static String format(String template, String... args) {
        Optional<String> templateContent = Optional.ofNullable(propertiesConfig.getConfigValue(template));
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
}
