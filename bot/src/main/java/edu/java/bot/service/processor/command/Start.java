package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class Start extends BaseCommand {
    @Override
    public String name() {
        return "/start";
    }

    @Override
    public String description() {
        return "зарегистрироваться в системе";
    }

    @Override
    protected String processImpl(User user) {
        return "Вы зарегистрированы";
    }
}
