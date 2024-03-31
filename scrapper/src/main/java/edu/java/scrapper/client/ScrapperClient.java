package edu.java.scrapper.client;

import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

@Retry(name = "retryGithub")
public interface ScrapperClient {
    @PostExchange("/updates")
    void sendUpdate(@RequestBody LinkUpdateRequest request);
}
