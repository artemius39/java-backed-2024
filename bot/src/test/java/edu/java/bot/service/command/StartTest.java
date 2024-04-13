package edu.java.bot.service.command;

import edu.java.bot.client.BotClient;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class StartTest {
    @Test
    void startPrintsSuccessMessageWhenUserIsRegistered() {
        BotClient botClient = mock(BotClient.class);
        Start start = new Start(botClient);

        String response = start.process("/start", 0);

        assertThat(response).isEqualTo(
            "Вы успешно зарегистрировались! Введите /help для того, чтобы увидеть список доступных команд");
    }
}
