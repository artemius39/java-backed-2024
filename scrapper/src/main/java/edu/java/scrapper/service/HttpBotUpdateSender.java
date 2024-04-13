package edu.java.scrapper.service;

import edu.java.scrapper.client.ScrapperClient;
import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HttpBotUpdateSender implements BotUpdateSender {
    private final ScrapperClient scrapperClient;

    @Override
    public void send(LinkUpdateRequest request) {
        scrapperClient.sendUpdate(request);
    }
}
