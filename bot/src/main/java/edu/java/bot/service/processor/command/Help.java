package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Help extends BaseCommand {
    private final List<Command> otherCommands;

    public Help(List<Command> otherCommands) {
        this.otherCommands = otherCommands;
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
    protected String processImpl(User user) {
        return "Доступные команды:\n"
               + Stream.concat(Stream.of(this), otherCommands.stream())
                       .map(command -> command.name() + " -- " + command.description())
                       .collect(Collectors.joining("\n"));
    }
}
