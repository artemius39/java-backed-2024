package edu.java.scrapper.exception;

public class ApiRequestRateExceededException extends RuntimeException {
    public ApiRequestRateExceededException(String ip) {
        super("Too many API requests from ip " + ip);
    }
}
