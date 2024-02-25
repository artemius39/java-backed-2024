package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.BotService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
public class BotController implements UpdatesListener {
    private final TelegramBot bot;
    private final BotService botService;

    public BotController(TelegramBot bot, BotService botService) {
        this.bot = bot;
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
                String responseMessage = botService.process(update);
                log.info(
                    "Responding to \"{}\" from chat no. {} with \"{}\"",
                    message.text(), chatId, responseMessage
                );
                bot.execute(new SendMessage(chatId, responseMessage));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
