package edu.java.bot.repository;

import java.util.Optional;
import edu.java.bot.model.User;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
}
