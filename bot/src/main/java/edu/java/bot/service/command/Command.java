package edu.java.bot.service.command;

public interface Command {
    String process(String message, long chatId);

    String name();

    String description();
}
