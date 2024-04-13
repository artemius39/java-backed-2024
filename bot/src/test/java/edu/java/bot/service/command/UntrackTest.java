package edu.java.bot.service.command;

import edu.java.bot.client.BotClient;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UntrackTest {
    @Test
    void linkIsUntracked() {
        BotClient botClient = mock(BotClient.class);
        when(botClient.removeLink(new RemoveLinkRequest(URI.create("example.com")), 0))
            .thenReturn(new LinkResponse(0, URI.create("example.com")));
        Untrack untrack = new Untrack(botClient);

        String response = untrack.process("example.com", 0);

        assertThat(response).isEqualTo("Сайт удалён из отслеживания");
    }

    @Test
    void errorMessageIsPrintedOnWrongFormat() {
        BotClient botClient = mock(BotClient.class);
        Untrack untrack = new Untrack(botClient);

        String response = untrack.process("", 0);

        assertThat(response).isEqualTo("Неверный формат команды. Правильный формат: `/untrack example.com`");
    }
}
