package edu.java.bot.service.processor;

import edu.java.bot.model.User;
import edu.java.bot.service.processor.command.CommandProcessor;
import edu.java.bot.service.processor.command.Help;
import edu.java.bot.service.processor.command.List;
import edu.java.bot.service.processor.command.Start;
import edu.java.bot.service.processor.command.Track;
import edu.java.bot.service.processor.command.Untrack;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ProcessCommand implements MessageProcessor {

    private final Map<String, CommandProcessor> commandProcessors;

    public ProcessCommand(Start start, Help help, List list, Track track, Untrack untrack) {
        commandProcessors = Map.of(
            "/start", start,
            "/help", help,
            "/list", list,
            "/track", track,
            "/untrack", untrack
        );
    }

    @Override
    public String process(String command, User user) {
        assert user.getState() == User.State.WAITING_FOR_COMMAND;
        CommandProcessor commandProcessor = commandProcessors.get(command);
        if (commandProcessor == null) {
            return "Неизвестная команда. Введите /help, чтобы увидеть список доступных команд";
        } else {
            return commandProcessor.process(user);
        }
    }
}
