package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.BotService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class BotController implements UpdatesListener {
    private static final Logger LOGGER = LogManager.getLogger("Bot Controller");

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
            long chatId = message.chat().id();
            LOGGER.info("Processing message \"{}\" from chat no. {}", message.text(), chatId);
            String responseMessage = botService.process(update);
            LOGGER.info("Responding to \"{}\" from chat no. {} with \"{}\"", message.text(), chatId, responseMessage);
            bot.execute(new SendMessage(chatId, responseMessage));
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
