package com.mkierzkowski.vboard_back.exception;

import com.mkierzkowski.vboard_back.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@SuppressWarnings("rawtypes, unchecked")
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(VBoardException.EntityNotFoundException.class)
    public final ResponseEntity handleNotFoundExceptions(Exception ex, WebRequest request) {
        Response response = Response.notFound();
        response.addErrorMsgToResponse(ex);
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VBoardException.DuplicateEntityException.class)
    public final ResponseEntity handleDuplicateEntityExceptions(Exception ex, WebRequest request) {
        Response response = Response.duplicateEntity();
        response.addErrorMsgToResponse(ex);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(VBoardException.VerificationEmailException.class)
    public final ResponseEntity handleVerificationEmailExceptions(Exception ex, WebRequest request) {
        Response response = Response.verificationEmailError();
        response.addErrorMsgToResponse(ex);
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(VBoardException.ExpiredVerificationTokenException.class)
    public final ResponseEntity handleExpiredVerificationTokenExceptions(Exception ex, WebRequest request) {
        Response response = Response.expiredVerificationToken();
        response.addErrorMsgToResponse(ex);
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(VBoardException.NotVerifiedException.class)
    public final ResponseEntity handleNotVerifiedExceptions(Exception ex, WebRequest request) {
        Response response = Response.notVerified();
        response.addErrorMsgToResponse(ex);
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(VBoardException.InvalidException.class)
    public final ResponseEntity handleInvalidTokenException(Exception ex, WebRequest request) {
        Response response = Response.invalidToken();
        response.addErrorMsgToResponse(ex);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VBoardException.ForbiddenException.class)
    public final ResponseEntity handleNotAuthorizedException(Exception ex, WebRequest request) {
        Response response = Response.forbidden();
        response.addErrorMsgToResponse(ex);
        return new ResponseEntity(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(VBoardException.OperationFailedException.class)
    public final ResponseEntity handleOperationFailedException(Exception ex, WebRequest request) {
        Response response = Response.failed();
        response.addErrorMsgToResponse(ex);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }
}
