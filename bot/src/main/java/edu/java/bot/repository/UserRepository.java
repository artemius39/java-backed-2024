package edu.java.bot.repository;

import edu.java.bot.model.User;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);
}
