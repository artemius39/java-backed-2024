package edu.java.scrapper.repository;

import edu.java.scrapper.model.Link;
import java.util.List;

public interface LinkRepository {
    Link add(Link link);

    void remove(Link link);

    List<Link> findAll();
}
