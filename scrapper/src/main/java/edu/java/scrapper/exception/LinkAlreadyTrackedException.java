package edu.java.scrapper.exception;

import java.net.URI;

public class LinkAlreadyTrackedException extends RuntimeException {
    public LinkAlreadyTrackedException(URI url, long chatId) {
        super("Ссылка " + url + " уже отслеживается в чате " + chatId);
    }
}
