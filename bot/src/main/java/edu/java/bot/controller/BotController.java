package edu.java.bot.controller;

import edu.java.bot.dto.LinkUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class BotController {
    @GetMapping("/updates")
    void processUpdate(LinkUpdate update) {
       log.info("Received updates: {}", update);
    }
}
