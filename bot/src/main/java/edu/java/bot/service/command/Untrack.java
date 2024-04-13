package edu.java.bot.service.command;

import edu.java.bot.client.BotClient;
import edu.java.bot.dto.request.RemoveLinkRequest;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Untrack implements Command {
    private static final String USAGE = "`/untrack example.com`";
    private static final String ILLEGAL_FORMAT_MESSAGE = "Неверный формат команды. Правильный формат: " + USAGE;

    private final BotClient botClient;

    @Override
    public String name() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "убрать сайт из отслеживания. Пример: " + USAGE;
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
        botClient.removeLink(new RemoveLinkRequest(link), chatId);
        return "Сайт удалён из отслеживания";
    }
}
