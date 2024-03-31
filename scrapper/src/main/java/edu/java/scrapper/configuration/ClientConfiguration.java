package edu.java.scrapper.configuration;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.ScrapperClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.dto.bot.ApiErrorResponse;
import edu.java.scrapper.exception.ApiException;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
@ConfigurationProperties(prefix = "client")
@Setter
public class ClientConfiguration {
    private Map<String, ClientConfig> clients;

    @Bean
    public GithubClient githubClient() {
        ClientConfig config = getConfig("github");
        WebClient webClient = WebClient.builder()
            .baseUrl(config.baseUrl)
            .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(GithubClient.class);
    }

    @Bean
    public Retry githubRetry() {
        return createRetry("github", "retryGithub");
    }

    @Bean
    public Retry stackOverflowRetry() {
        return createRetry("stackoverflow", "retryStackOverflow");
    }

    @Bean
    public Retry scrapperRetry() {
        return createRetry("scrapper", "retryScrapper");
    }

    private Retry createRetry(String clientName, String retryName) {
        return RetryRegistry.of(createRetryConfig(clientName)).retry(retryName);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        ClientConfig config = getConfig("stackoverflow");
        HttpClient httpClient = HttpClient.create()
            .baseUrl(config.baseUrl)
            .compress(true);

        WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(StackOverflowClient.class);
    }

    @Bean
    public ScrapperClient scrapperClient() {
        ClientConfig config = getConfig("scrapper");
        WebClient webClient = WebClient.builder()
            .baseUrl(config.baseUrl)
            .defaultStatusHandler(
                HttpStatusCode::is4xxClientError,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ApiException(apiErrorResponse)))
            )
            .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ScrapperClient.class);
    }

    @NotNull
    private ClientConfig getConfig(String clientName) {
        ClientConfig config = clients.get(clientName);
        if (config == null) {
            throw new RuntimeException("Missing config for " + clientName + " client");
        }
        return config;
    }

    private RetryConfig createRetryConfig(String clientName) {
        RetryPolicy retryPolicy = getConfig(clientName).retryPolicy;
        if (retryPolicy.type == RetryPolicy.Type.NONE) {
            return RetryConfig.custom()
                .maxAttempts(1)
                .build();
        }
        IntervalFunction intervalFunction = switch (retryPolicy.type) {
            case CONSTANT -> IntervalFunction.of(retryPolicy.backoffPeriod);
            case LINEAR -> attemptNo -> retryPolicy.initialInterval + (long) attemptNo * retryPolicy.increment;
            case EXPONENTIAL -> IntervalFunction.ofExponentialBackoff(retryPolicy.initialInterval, retryPolicy.multiplier);
            default -> throw new IllegalStateException("Unexpected policy type: " + retryPolicy.type);
        };

        return RetryConfig.custom()
            .maxAttempts(retryPolicy.maxAttempts)
            .intervalFunction(intervalFunction)
            .build();
    }

    @Setter
    @Getter
    public static class ClientConfig {
        private String baseUrl;
        private RetryPolicy retryPolicy;
    }

    @Setter
    @Getter
    public static class RetryPolicy {
        private Type type;
        private int initialInterval;
        private int increment;
        private int maxAttempts = 1;
        private int backoffPeriod;
        private double multiplier;

        public enum Type {
            NONE, CONSTANT, LINEAR, EXPONENTIAL
        }
    }
}
