package edu.java.scrapper.repository;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import java.util.Collection;

public interface LinkRepository {
    Link add(Link link, User user);

    Link remove(Link link, User user);

    Collection<Link> findByUserId(Long userId);
}
