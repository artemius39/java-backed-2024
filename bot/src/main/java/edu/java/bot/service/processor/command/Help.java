package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class Help extends BaseCommandProcessor {
    @Override
    protected String processImpl(User user) {
        return """
            Доступные команды:
            /start -- зарегистрироваться в системе
            /help -- список команд
            /track -- начать отслеживать сайт
            /untrack -- прекратить отслеживать сайт
            /list -- список отслеживаемых сайтов""";
    }
}
