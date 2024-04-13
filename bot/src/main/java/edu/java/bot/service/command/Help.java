package edu.java.bot.service.command;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class Help implements Command {
    private final List<Command> commands;

    public Help(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String name() {
        return "/help";
    }

    @Override
    public String description() {
        return "список всех команд";
    }

    @Override
    public String process(String message, long chatId) {
        return "Доступные команды:\n"
               + Stream.concat(Stream.of(this), commands.stream())
                       .map(command -> command.name() + " -- " + command.description())
                       .collect(Collectors.joining("\n"));
    }
}
