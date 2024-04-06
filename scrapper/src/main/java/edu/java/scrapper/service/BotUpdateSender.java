package edu.java.scrapper.service;

import edu.java.scrapper.dto.bot.LinkUpdateRequest;

public interface BotUpdateSender {
    void send(LinkUpdateRequest request);
}
