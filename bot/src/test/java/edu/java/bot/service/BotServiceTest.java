package edu.java.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import edu.java.bot.service.processor.MessageProcessor;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotServiceTest {
    @Test
    void userGetsAutomaticallyRegistered() {
        User user = new User();
        user.setId(0L);
        user.setState(User.State.WAITING_FOR_COMMAND);
        MessageProcessor messageProcessor = mock(MessageProcessor.class);
        when(messageProcessor.supportedState()).thenReturn(User.State.WAITING_FOR_COMMAND);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(0L);
        BotService botService = new BotService(userRepository, List.of(messageProcessor));

        botService.process(update);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void messageGetsForwardedToCorrespondingMessageProcessor() {
        User user = new User();
        user.setId(0L);
        user.setState(User.State.WAITING_FOR_COMMAND);

        MessageProcessor messageProcessor1 = mock(MessageProcessor.class);
        when(messageProcessor1.supportedState()).thenReturn(User.State.WAITING_FOR_COMMAND);
        MessageProcessor messageProcessor2 = mock(MessageProcessor.class);
        when(messageProcessor2.supportedState()).thenReturn(User.State.WAITING_FOR_LINK_TO_ADD);
        MessageProcessor messageProcessor3 = mock(MessageProcessor.class);
        when(messageProcessor3.supportedState()).thenReturn(User.State.WAITING_FOR_LINK_TO_REMOVE);

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("text");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(0L);

        BotService botService = new BotService(userRepository, List.of(messageProcessor1, messageProcessor2, messageProcessor3));

        botService.process(update);

        verify(messageProcessor1, times(1)).process("text", user);
        verify(messageProcessor2, times(0)).process("text", user);
        verify(messageProcessor3, times(0)).process("text", user);
    }
}
