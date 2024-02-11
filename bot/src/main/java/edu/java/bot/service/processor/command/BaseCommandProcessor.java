package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;

abstract class BaseCommandProcessor implements CommandProcessor {
    @Override
    public String process(User user) {
        if (user.getState() != User.State.WAITING_FOR_COMMAND) {
            throw new IllegalStateException("Illegal user state");
        }
        return processImpl(user);
    }

    protected abstract String processImpl(User user);
}
