package edu.java.bot.service.processor;

import edu.java.bot.model.User;

abstract class BaseMessageProcessor implements MessageProcessor {
    @Override
    public String process(String message, User user) {
        if (user.getState() != supportedState()) {
            throw new IllegalStateException("Illegal user state");
        }
        return processImpl(message, user);
    }

    protected abstract String processImpl(String message, User user);

    protected abstract User.State supportedState();
}
