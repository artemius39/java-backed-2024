package edu.java.bot.client;

import edu.java.bot.configuration.ClientConfiguration;
import org.junit.jupiter.api.Test;

public class BotClientTest {
    @Test
    void test() {
        BotClient client = new ClientConfiguration().botClient("http://localhost:8080");
        client.addLink(null);
    }
}
