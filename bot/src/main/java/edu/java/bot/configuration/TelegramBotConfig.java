package edu.java.bot.configuration;

import java.util.List;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.service.processor.command.Command;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {
    @Bean
    public TelegramBot getTelegramBot(ApplicationConfig applicationConfig, List<Command> commands) {
        TelegramBot telegramBot = new TelegramBot(applicationConfig.telegramToken());
        BotCommand[] botCommands = commands.stream()
            .map(command -> new BotCommand(command.name(), command.description()))
            .toArray(BotCommand[]::new);
        telegramBot.execute(new SetMyCommands(botCommands));
        return telegramBot;
    }
}
