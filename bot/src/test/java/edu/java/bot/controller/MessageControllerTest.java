package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.BotService;
import edu.java.bot.service.BotServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MessageControllerTest {
    @Test
    void nonMessageUpdatesAreIgnored() {
        Update update = mock(Update.class);
        TelegramBot telegramBot = mock(TelegramBot.class);
        BotServiceImpl botService = mock(BotServiceImpl.class);

        MessageController controller = new MessageController(telegramBot, botService);

        controller.process(List.of(update));

        verify(botService, never()).processMessage(update);
    }

    @Test
    void messageUpdatesAreBeingProcessed() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(0L);

        BotService botService = mock(BotServiceImpl.class);
        TelegramBot bot = mock(TelegramBot.class);
        MessageController messageController = new MessageController(bot, botService);

        messageController.process(List.of(update));

        verify(botService).processMessage(update);
    }
}
