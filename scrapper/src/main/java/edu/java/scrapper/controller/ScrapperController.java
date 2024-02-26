package edu.java.scrapper.controller;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import edu.java.scrapper.exception.InvalidParameterException;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class ScrapperController {
    private static final String INVALID_CHAT_ID_MESSAGE = "Invalid chat id";

    @PostMapping("/tg-chat/{id}")
    public void registerChat(@PathVariable String id) {
        long chatId = parseLong(id, "chat id");
        log.info("Registered chat no. {}", chatId);
    }

    @DeleteMapping("/tg-chat/{id}")
    public void deleteChat(@PathVariable String id) {
        long chatId = parseLong(id, INVALID_CHAT_ID_MESSAGE);
        log.info("Deleted chat no. {}", chatId);
    }

    @GetMapping("/links")
    public ListLinksResponse getAllLinks(@RequestParam(name = "Tg-Chat-Id") String id) {
        long chatId = parseLong(id, INVALID_CHAT_ID_MESSAGE);
        ListLinksResponse response = new ListLinksResponse(List.of());
        log.info("Fetched all links: {} for chat no. {}", response, chatId);
        return response;
    }

    @PostMapping("/links")
    public LinkResponse addLink(
        @RequestParam(name = "Tg-Chat-Id") String id,
        @RequestBody AddLinkRequest link
    ) {
        long chatId = parseLong(id, INVALID_CHAT_ID_MESSAGE);
        log.info("Added link {} to chat no. {}", link, id);
        return new LinkResponse(chatId, link.url());
    }

    @DeleteMapping("/links")
    public LinkResponse removeLink(
        @RequestParam(name = "Tg-Chat-Id") String id,
        @RequestBody RemoveLinkRequest link
    ) {
        long chatId = parseLong(id, INVALID_CHAT_ID_MESSAGE);
        log.info("Removed link {} from chat no. {}", link, id);
        return new LinkResponse(chatId, link.url());
    }

    private long parseLong(String number, String description) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(description + ": " + number);
        }
    }
}
