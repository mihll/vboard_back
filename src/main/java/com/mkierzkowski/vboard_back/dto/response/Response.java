package com.mkierzkowski.vboard_back.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mkierzkowski.vboard_back.util.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {

    private Status status;
    private T payload;
    private Object errors;
    private Object metadata;

    public static <T> Response<T> notFound() {
        Response<T> response = new Response<>();
        response.setStatus(Status.NOT_FOUND);
        return response;
    }

    public static <T> Response<T> notVerified() {
        Response<T> response = new Response<>();
        response.setStatus(Status.NOT_VERIFIED);
        return response;
    }

    public static <T> Response<T> duplicateEntity() {
        Response<T> response = new Response<>();
        response.setStatus(Status.DUPLICATE_ENTITY);
        return response;
    }

    public static <T> Response<T> verificationEmailError() {
        Response<T> response = new Response<>();
        response.setStatus(Status.VERIFICATION_EMAIL_ERROR);
        return response;
    }

    public static <T> Response<T> expiredVerificationToken() {
        Response<T> response = new Response<>();
        response.setStatus(Status.EXPIRED);
        return response;
    }

    public static <T> Response<T> invalidToken() {
        Response<T> response = new Response<>();
        response.setStatus(Status.INVALID);
        return response;
    }

    public static <T> Response<T> forbidden() {
        Response<T> response = new Response<>();
        response.setStatus(Status.FORBIDDEN);
        return response;
    }

    public static <T> Response<T> failed() {
        Response<T> response = new Response<>();
        response.setStatus(Status.FAILED);
        return response;
    }

    public void addErrorMsgToResponse(Exception ex) {
        ResponseError error = new ResponseError();
        Map<String, String> strings = getMessageAndDetails(ex);
        if (strings.containsKey("details")) {
            error.setDetails(strings.get("details"));
        }
        error.setMessage(strings.get("message"));
        error.setTimestamp(DateUtils.today());
        setErrors(error);
    }

    public static Map<String, String> getMessageAndDetails(Exception ex) {
        Map<String, String> result = new HashMap<>();
        int indexOfFirstSpace = ex.getMessage().indexOf(' ');
        if (indexOfFirstSpace != -1) {
            result.put("message",(ex.getMessage().substring(0, indexOfFirstSpace)));
            result.put("details",(ex.getMessage().substring(indexOfFirstSpace + 1)));
        } else {
            result.put("message",(ex.getMessage()));
        }
        return result;
    }

    public enum Status {
        OK, NOT_FOUND, DUPLICATE_ENTITY, VERIFICATION_EMAIL_ERROR, EXPIRED, NOT_VERIFIED, INVALID, FORBIDDEN, FAILED
    }
}