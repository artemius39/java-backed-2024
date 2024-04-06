package edu.java.scrapper.configuration;

import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfiguration {
    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate(
        ProducerFactory<String, LinkUpdateRequest> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic updatesTopic(ApplicationConfig applicationConfig) {
        ApplicationConfig.Topic topic = applicationConfig.updatesTopic();
        return TopicBuilder.name(topic.name())
            .replicas(topic.replicas())
            .partitions(topic.partitions())
            .build();
    }
}
