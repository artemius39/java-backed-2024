package edu.java.bot.service.processor;

import edu.java.bot.model.User;
import edu.java.bot.service.processor.command.Command;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProcessCommand extends BaseMessageProcessor {

    private final Map<String, Command> commandProcessors;

    public ProcessCommand(List<Command> commands) {
        commandProcessors = commands.stream()
                .collect(Collectors.toMap(Command::name, Function.identity()));
    }

    @Override
    public User.State supportedState() {
        return User.State.WAITING_FOR_COMMAND;
    }

    @Override
    protected String processImpl(String command, User user) {
        Command commandProcessor = commandProcessors.get(command);
        if (commandProcessor == null) {
            return "Неизвестная команда. Введите /help, чтобы увидеть список доступных команд";
        } else {
            return commandProcessor.process(user);
        }
    }
}
