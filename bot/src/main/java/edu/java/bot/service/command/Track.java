package edu.java.bot.service.command;

import edu.java.bot.client.BotClient;
import edu.java.bot.dto.request.AddLinkRequest;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Track implements Command {
    private static final String USAGE = "`/track example.com`";
    private static final String ILLEGAL_FORMAT_MESSAGE = "Неверный формат команды. Правильный формат: " + USAGE;

    private final BotClient botClient;

    @Override
    public String name() {
        return "/track";
    }

    @Override
    public String description() {
        return "добавить сайт в отслеживание. Пример: " + USAGE;
    }

    @Override
    public String process(String message, long chatId) {
        if (message.isEmpty()) {
            return ILLEGAL_FORMAT_MESSAGE;
        }
        URI link;
        try {
            link = URI.create(message);
        } catch (IllegalArgumentException e) {
            return ILLEGAL_FORMAT_MESSAGE;
        }
        botClient.addLink(new AddLinkRequest(link), chatId);
        return "Сайт добавлен в отслеживаемые";
    }
}
