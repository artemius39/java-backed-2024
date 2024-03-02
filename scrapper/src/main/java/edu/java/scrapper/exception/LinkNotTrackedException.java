package edu.java.scrapper.exception;

public class LinkNotTrackedException extends RuntimeException {
    public LinkNotTrackedException(String link) {
        super("Link " + link + " is not tracked");
    }
}
