package edu.java.scrapper.repository;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

public interface LinkRepository {
    Link add(Link link, User user);

    Link remove(Link link, User user);

    Collection<Link> findByUserId(Long userId);

    Collection<Link> findByLastUpdateTime(Duration timeSinceLastUpdate);

    List<Long> findByLinkId(Long id);
}
