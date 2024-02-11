package edu.java.bot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.repository.InMemoryUserRepository;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.processor.AddLink;
import edu.java.bot.service.processor.MessageProcessor;
import edu.java.bot.service.processor.ProcessCommand;
import edu.java.bot.service.processor.RemoveLink;
import java.util.HashSet;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    private final UserRepository userRepository = new InMemoryUserRepository();
    private final Map<User.State, MessageProcessor> messageProcessors;

    public BotService(ProcessCommand processCommand, AddLink addLink, RemoveLink removeLink) {
        messageProcessors = Map.of(
            User.State.WAITING_FOR_COMMAND, processCommand,
            User.State.WAITING_FOR_LINK_TO_ADD, addLink,
            User.State.WAITING_FOR_LINK_TO_REMOVE, removeLink
        );
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
        MessageProcessor messageProcessor = messageProcessors.get(user.getState());
        return messageProcessor.process(message.text(), user);
    }
}
