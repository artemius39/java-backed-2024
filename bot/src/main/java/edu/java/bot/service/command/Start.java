package edu.java.bot.service.command;

import edu.java.bot.client.BotClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Start implements Command {
    private final BotClient botClient;

    @Override
    public String name() {
        return "/start";
    }

    @Override
    public String description() {
        return "зарегистрироваться в системе";
    }

    @Override
    public String process(String message, long chatId) {
        botClient.registerChat(chatId);
        return "Вы успешно зарегистрировались! Введите /help для того, чтобы увидеть список доступных команд";
    }
}
