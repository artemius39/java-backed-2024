package edu.java.scrapper.dto.response;

import java.util.Arrays;
import java.util.List;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    List<String> stackTrace
) {
    public ApiErrorResponse(String description, String code, Throwable cause) {
        this(
            description,
            code,
            cause.getClass().getSimpleName(),
            cause.getMessage(),
            Arrays.stream(cause.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }
}
