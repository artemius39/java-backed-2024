package edu.java.scrapper.repository;

import edu.java.scrapper.model.User;

public interface UserRepository {
    User add(User user);

    void remove(User user);
}
