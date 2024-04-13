package edu.java.bot.service.command;

import edu.java.bot.client.BotClient;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrackTest {
    @Test
    void trackSuccessfullyAddsLink() {
        BotClient botClient = mock(BotClient.class);
        when(botClient.addLink(new AddLinkRequest(URI.create("example.com")), 0))
            .thenReturn(new LinkResponse(0, URI.create("example.com")));
        Track track = new Track(botClient);

        String response = track.process("example.com", 0);

        assertThat(response).isEqualTo("Сайт добавлен в отслеживаемые");
    }

    @Test
    void errorMessageIsPrintedOnWrongFormat() {
        BotClient botClient = mock(BotClient.class);
        Track track = new Track(botClient);

        String response = track.process("", 0);

        assertThat(response).isEqualTo("Неверный формат команды. Правильный формат: `/track example.com`");
    }
}
