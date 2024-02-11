package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class Start extends BaseCommandProcessor {
    @Override
    protected String processImpl(User user) {
        return "Вы зарегистрированы";
    }
}
