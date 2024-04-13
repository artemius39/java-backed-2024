package edu.java.bot.service.command;

import edu.java.bot.client.BotClient;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListLinksResponse;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListTest {
    @Test
    void listPrintsSpecialMessageIfUserHasNoLinks() {
        BotClient botClient = mock(BotClient.class);
        when(botClient.listTrackedLinks(0))
            .thenReturn(new ListLinksResponse(List.of(), 0));

        ListCommand list = new ListCommand(botClient);

        String response = list.process("/list", 0);

        assertThat(response).isEqualTo("Вы не отслеживаете никакие сайты");
    }

    @Test
    void listPrintsLinksIfUserHasLinks() {
        BotClient botClient = mock(BotClient.class);
        when(botClient.listTrackedLinks(0))
            .thenReturn(new ListLinksResponse(List.of(new LinkResponse(1, URI.create("example.com"))), 1));
        ListCommand list = new ListCommand(botClient);

        String response = list.process("/help", 0);

        assertThat(response).isEqualTo("example.com");
    }
}
