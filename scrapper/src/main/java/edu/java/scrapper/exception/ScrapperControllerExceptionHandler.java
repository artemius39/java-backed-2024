package edu.java.scrapper.exception;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ScrapperControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserAlreadyRegisteredException.class)
    protected ResponseEntity<ApiErrorResponse> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException e) {
        ApiErrorResponse response = new ApiErrorResponse("User already registered", "400", e);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<ApiErrorResponse> handleInvalidParameterException(InvalidParameterException e) {
        ApiErrorResponse response = new ApiErrorResponse("Invalid request parameter", "400", e);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ApiErrorResponse> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException e
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
            "Missing required parameter",
            "400",
            "Parameter " + e.getParameterName() + " is required",
            e
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    protected ResponseEntity<ApiErrorResponse> handleHttpMessageConversionException(
        HttpMessageConversionException e
    ) {
        ApiErrorResponse response = new ApiErrorResponse("Invalid request body", "400", e);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ChatNotFoundException.class)
    protected ResponseEntity<ApiErrorResponse> handleChatNotFoundException(ChatNotFoundException e) {
        ApiErrorResponse response = new ApiErrorResponse("Chat not found", "404", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
