package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;

public interface CommandProcessor {
    String process(User user);
}
