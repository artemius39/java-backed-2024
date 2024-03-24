package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.model.jpa.Link;
import edu.java.scrapper.model.jpa.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long> {
    List<Long> findUsersByLinksContains(Link link);
}
