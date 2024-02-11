package edu.java.bot.service.processor;

import edu.java.bot.model.User;

public interface MessageProcessor {
    String process(String message, User user);
}
