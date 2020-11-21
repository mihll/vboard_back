package com.mkierzkowski.vboard_back.exception;

import com.mkierzkowski.vboard_back.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("rawtypes, unchecked")
    @ExceptionHandler(VBoardException.EntityNotFoundException.class)
    public final ResponseEntity handleNotFoundExceptions(Exception ex, WebRequest request) {
        Response response = Response.notFound();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("rawtypes, unchecked")
    @ExceptionHandler(VBoardException.DuplicateEntityException.class)
    public final ResponseEntity handleDuplicateEntityExceptions(Exception ex, WebRequest request) {
        Response response = Response.duplicateEntity();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }

    @SuppressWarnings("rawtypes, unchecked")
    @ExceptionHandler(VBoardException.VerificationEmailException.class)
    public final ResponseEntity handleVerificationEmailExceptions(Exception ex, WebRequest request) {
        Response response = Response.verificationEmailError();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
