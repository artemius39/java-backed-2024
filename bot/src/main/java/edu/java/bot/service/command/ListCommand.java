package edu.java.bot.service.command;

import edu.java.bot.client.BotClient;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListLinksResponse;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListCommand implements Command {
    private final BotClient botClient;

    @Override
    public String name() {
        return "/list";
    }

    @Override
    public String description() {
        return "список отслеживаемых сайтов";
    }

    @Override
    public String process(String message, long chatId) {
        ListLinksResponse response = botClient.listTrackedLinks(chatId);
        String links = response.links().stream()
            .map(LinkResponse::url)
            .map(URI::toString)
            .collect(Collectors.joining("\n"));
        return links.isEmpty() ? "Вы не отслеживаете никакие сайты" : links;
    }
}
