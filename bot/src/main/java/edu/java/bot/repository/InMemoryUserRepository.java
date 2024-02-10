package edu.java.bot.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import edu.java.bot.model.User;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users;

    public InMemoryUserRepository() {
        users = new HashMap<>();
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
}
