package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {
    @Bean
    public TelegramBot getTelegramBot(ApplicationConfig applicationConfig) {
        TelegramBot telegramBot = new TelegramBot(applicationConfig.telegramToken());
        telegramBot.execute(new SetMyCommands(
            new BotCommand("/start", "Начать работу"),
            new BotCommand("/help", "Вывести список доступных команд"),
            new BotCommand("/list", "Вывести список отслеживаемых сайтов"),
            new BotCommand("/track", "Добавить сайт в отслеживание"),
            new BotCommand("/untrack", "Прекратить отслеживание сайта")
        ));
        return telegramBot;
    }
}
