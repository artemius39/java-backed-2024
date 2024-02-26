package edu.java.scrapper.exception;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
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
}
