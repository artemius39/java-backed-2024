package edu.java.bot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.service.BotService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class UpdatesListener {
    private final ObjectMapper objectMapper;
    private final BotService botService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(
        topics = "${app.updates-topic.name}",
        autoStartup = "true",
        containerFactory = "containerFactory",
        groupId = "main"
    )
    public void sendUpdates(@Payload String payload) {
        try {
            LinkUpdateRequest request = objectMapper.readValue(payload, LinkUpdateRequest.class);
            botService.sendUpdates(request);
        } catch (JsonProcessingException e) {
            kafkaTemplate.send("updates_dlq", payload);
            log.error("Error parsing JSON", e);
        }
    }
}
