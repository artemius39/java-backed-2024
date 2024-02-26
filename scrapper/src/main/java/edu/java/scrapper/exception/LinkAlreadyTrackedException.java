package edu.java.scrapper.exception;

public class LinkAlreadyTrackedException extends RuntimeException {
    public LinkAlreadyTrackedException(String link) {
        super("Ссылка " + link + " уже отслеживается");
    }
}
