package edu.java.bot.repository;

import edu.java.bot.model.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);
}
