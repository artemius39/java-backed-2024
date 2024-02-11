package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class Help implements CommandProcessor {
    @Override
    public String process(User user) {
        assert user.getState() == User.State.WAITING_FOR_COMMAND;
        return """
            Доступные команды:
            /start -- зарегистрироваться в системе
            /help -- список команд
            /track -- начать отслеживать сайт
            /untrack -- прекратить отслеживать сайт
            /list -- список отслеживаемых сайтов""";
    }
}
