package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dto.request.LinkUpdateRequest;

public interface BotService {
    void processMessage(Update update);

    void sendUpdates(LinkUpdateRequest update);
}
