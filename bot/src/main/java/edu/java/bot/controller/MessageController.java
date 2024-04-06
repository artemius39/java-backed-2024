package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.BotService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class MessageController implements UpdatesListener {
    private final BotService botService;

    public MessageController(TelegramBot bot, BotService botService) {
        this.botService = botService;
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            Message message = update.message();
            if (message != null) {
                long chatId = message.chat().id();
                log.info("Processing message \"{}\" from chat no. {}", message.text(), chatId);
                botService.processMessage(update);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
