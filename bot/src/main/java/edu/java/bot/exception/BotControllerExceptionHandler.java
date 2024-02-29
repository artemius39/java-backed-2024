package edu.java.bot.exception;

import edu.java.bot.dto.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class BotControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ChatNotFoundException.class)
    protected ResponseEntity<ApiErrorResponse> handleChatNotFoundException(ChatNotFoundException e) {
        ApiErrorResponse response = new ApiErrorResponse("Chat not found", "400", e);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e
    ) {
        ApiErrorResponse response = new ApiErrorResponse("Invalid JSON", "400", e);
        return ResponseEntity.badRequest().body(response);
    }
}
