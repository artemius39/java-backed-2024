package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;

public interface Command {
    String process(User user);

    String name();

    String description();
}
