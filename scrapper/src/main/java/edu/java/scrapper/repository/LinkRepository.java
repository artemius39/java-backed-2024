package edu.java.scrapper.repository;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;

public interface LinkRepository {
    Link add(Link link, User user);

    void remove(Link link, User user);
}
