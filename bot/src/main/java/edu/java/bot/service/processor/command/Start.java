package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class Start implements CommandProcessor {
    @Override
    public String process(User user) {
        assert user.getState() == User.State.WAITING_FOR_COMMAND;
        return "Вы зарегистрированы";
    }
}
