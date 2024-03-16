package edu.java.scrapper.service;

import edu.java.scrapper.model.Link;
import java.net.URI;
import java.util.Collection;

public interface LinkService {
    Link add(long chatId, URI url);

    Link remove(long chatId, URI url);

    Collection<Link> listAll(long chatId);
}
