package edu.java.scrapper.configuration;

import edu.java.scrapper.client.ScrapperClient;
import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import edu.java.scrapper.service.BotUpdateSender;
import edu.java.scrapper.service.HttpBotUpdateSender;
import edu.java.scrapper.service.KafkaBotUpdateSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class BotUpdateSenderConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public BotUpdateSender httpUpdateSender(ScrapperClient scrapperClient) {
        return new HttpBotUpdateSender(scrapperClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public BotUpdateSender kafkaUpdateSender(KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate) {
        return new KafkaBotUpdateSender(kafkaTemplate);
    }
}
