package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.command.Command;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotServiceImplTest {
    @Test
    void messageGetsForwardedToCorrespondingCommand() {
        Command command1 = mock(Command.class);
        when(command1.name())
            .thenReturn("/cmd1");
        Command command2 = mock(Command.class);
        when(command2.name())
            .thenReturn("/cmd2");

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message())
            .thenReturn(message);
        when(message.chat())
            .thenReturn(chat);
        when(chat.id())
            .thenReturn(1L);
        when(message.text())
            .thenReturn("/cmd1");
        TelegramBot bot = mock(TelegramBot.class);
        BotService botService = new BotServiceImpl(List.of(command1, command2), bot);

        botService.processMessage(update);

        verify(command1).process("", 1);
    }

    @Test
    void errorMessageIsPrintedWhenCommandIsNotSupported() {
        Command command1 = mock(Command.class);
        when(command1.name())
            .thenReturn("/cmd1");
        Command command2 = mock(Command.class);
        when(command2.name())
            .thenReturn("/cmd2");

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message())
            .thenReturn(message);
        when(message.chat())
            .thenReturn(chat);
        when(chat.id())
            .thenReturn(1L);
        when(message.text())
            .thenReturn("/cmd3");
        TelegramBot bot = mock(TelegramBot.class);
        BotService botService = new BotServiceImpl(List.of(command1, command2), bot);

        botService.processMessage(update);

        verify(bot).execute(argThat(
            req -> req.getParameters().get("chat_id").equals(1L)
                   && req.getParameters().get("text")
                       .equals("Неизвестная команда. Введите /help, чтобы получить список доступных команд")
        ));
    }
}
