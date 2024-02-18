package edu.java.scrapper.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@EnableScheduling
@Log4j2
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        log.info("update");
    }
}
