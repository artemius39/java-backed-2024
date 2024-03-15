package edu.java.scrapper.repository;

import edu.java.scrapper.model.Chat;
import java.util.List;

public interface ChatRepository {
    Chat add(Chat chat);

    void remove(Chat chat);

    List<Chat> findAll();
}
