package edu.java.bot.controller;

import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.service.BotService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@AllArgsConstructor
public class BotController {
    private final BotService botService;

    @GetMapping("/updates")
    void processUpdate(LinkUpdateRequest update) {
       log.info("Received updates: {}", update);
       botService.sendUpdates(update);
    }
}
