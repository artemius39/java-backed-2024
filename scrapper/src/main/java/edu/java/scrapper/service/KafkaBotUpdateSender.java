package edu.java.scrapper.service;

import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@AllArgsConstructor
public class KafkaBotUpdateSender implements BotUpdateSender {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    @Override
    public void send(LinkUpdateRequest request) {
        kafkaTemplate.send("updates", request);
    }
}
