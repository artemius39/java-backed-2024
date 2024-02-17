package edu.java.bot.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.processor.MessageProcessor;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    private final UserRepository userRepository;
    private final Map<User.State, MessageProcessor> messageProcessors;

    public BotService(UserRepository userRepository, List<MessageProcessor> messageProcessorList) {
        this.userRepository = userRepository;
        messageProcessors = messageProcessorList.stream()
                .collect(Collectors.toMap(MessageProcessor::supportedState, Function.identity()));
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
        if (messageProcessor == null) {
            throw new UnsupportedOperationException("User state " + user.getState() + " not supported");
        }
        return messageProcessor.process(message.text(), user);
    }
}
