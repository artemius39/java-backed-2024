package edu.java.bot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.repository.InMemoryUserRepository;
import edu.java.bot.repository.UserRepository;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    private final UserRepository userRepository = new InMemoryUserRepository();

    private final Map<User.State, MessageProcessor> MESSAGE_PROCESSORS = Map.of(
        User.State.WAITING_FOR_COMMAND, this::processCommand,
        User.State.WAITING_FOR_LINK_TO_ADD, this::addLink,
        User.State.WAITING_FOR_LINK_TO_REMOVE, this::removeLink
    );

    private final Map<String, CommandProcessor> COMMAND_PROCESSORS = Map.of(
        "/start", this::start,
        "/help", command -> help(),
        "/track", this::track,
        "/untrack", this::untrack,
        "/list", this::list
    );

    private String start(User user) {
        return "Вы зарегистрированы";
    }

    private String list(User user) {
        Set<String> links = user.getLinks();
        return links.isEmpty() ? "Вы не отслеживаете никакие сайты" : String.join("\n", links);
    }

    private String untrack(User user) {
        if (user.getLinks().isEmpty()) {
            return "Вы не отслеживаете никакие сайты";
        }
        user.setState(User.State.WAITING_FOR_LINK_TO_REMOVE);
        return "Введите ссылку, которую вы хотите прекратить отслеживать. Список отслеживаемых сайтов:\n" + list(user);
    }

    private String track(User user) {
        user.setState(User.State.WAITING_FOR_LINK_TO_ADD);
        return "Введите ссылку на сайт, который вы хотите отслеживать или \"Отмена\" для того, чтобы ничего не добавлять";
    }

    private String help() {
        return """
            Доступные команды:
            /start -- зарегистрироваться в системе
            /help -- список команд
            /track -- начать отслеживать сайт
            /untrack -- прекратить отслеживать сайт
            /list -- список отслеживаемых сайтов""";
    }

    public String process(Update update) {
        Message message = update.message();
        Long userId = message.chat().id();
        User user = userRepository.findById(userId).orElseGet(() -> {
            User newUser = new User();
            newUser.setId(userId);
            newUser.setState(User.State.WAITING_FOR_COMMAND);
            newUser.setLinks(new HashSet<>());
            return userRepository.save(newUser);
        });
        MessageProcessor messageProcessor = MESSAGE_PROCESSORS.get(user.getState());
        return messageProcessor.process(message.text(), user);
    }

    private String processCommand(String command, User user) {
        CommandProcessor commandProcessor = COMMAND_PROCESSORS.get(command);
        if (commandProcessor == null) {
            return "Неизвестная команда. Введите /help, чтобы увидеть список доступных команд";
        } else {
            return commandProcessor.process(user);
        }
    }

    private String removeLink(String link, User user) {
        if (link.equals("Отмена")) {
            user.setState(User.State.WAITING_FOR_COMMAND);
            return "Операция отменена";
        }
        if (user.removeLink(link)) {
            user.setState(User.State.WAITING_FOR_COMMAND);
            return "Ссылка была удалена";
        } else {
            return "Вы не отслеживаете эту ссылку. Попробуйте ещё раз или введите \"Отмена\", чтобы ничего не удалять";
        }
    }

    private String addLink(String link, User user) {
        user.setState(User.State.WAITING_FOR_COMMAND);
        if (link.equals("Отмена")) {
            return "Операция отменена";
        }
        if (user.addLink(link)) {
            return "Ссылка добавлена в отслеживание";
        } else {
            return "Вы уже отслеживаете эту ссылку";
        }
    }

    private interface MessageProcessor {
        String process(String message, User user);
    }

    private interface CommandProcessor {
        String process(User user);
    }
}
