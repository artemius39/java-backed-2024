package edu.java.scrapper.repository;

import edu.java.scrapper.model.Link;

public interface LinkRepository {
    Link add(Link link);

    void remove(Link link);
}
