package edu.java.scrapper.configuration;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.ScrapperClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.dto.bot.ApiErrorResponse;
import edu.java.scrapper.exception.ApiException;
import java.time.Duration;
import java.util.List;
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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

@Component
@ConfigurationProperties(prefix = "client")
@Setter
public class ClientConfiguration {
    private Map<String, ClientConfig> clients;

    @Bean
    public GithubClient githubClient() {
        ClientConfig config = getConfig("github");
        WebClient.Builder builder = WebClient.builder()
            .baseUrl(config.baseUrl);
        setRetry(builder, config);
        WebClient webClient = builder.build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(GithubClient.class);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        ClientConfig config = getConfig("stackoverflow");
        HttpClient httpClient = HttpClient.create()
            .baseUrl(config.baseUrl)
            .compress(true);

        WebClient.Builder builder = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient));
        setRetry(builder, config);
        WebClient webClient = builder.build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(StackOverflowClient.class);
    }

    @Bean
    public ScrapperClient scrapperClient() {
        ClientConfig config = getConfig("scrapper");
        WebClient.Builder builder = WebClient.builder()
            .baseUrl(config.baseUrl)
            .defaultStatusHandler(
                HttpStatusCode::is4xxClientError,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ApiException(apiErrorResponse)))
            );
        setRetry(builder, config);
        WebClient webClient = builder
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

    private void setRetry(WebClient.Builder builder, ClientConfig config) {
        RetryPolicy policy = config.retryPolicy;
        if (policy.type == RetryPolicy.Type.NONE) {
            return;
        }
        Retry retry = switch (policy.type) {
            case EXPONENTIAL -> exponentialRetry(policy);
            case LINEAR -> linearRetry(policy);
            case CONSTANT -> constantRetry(policy);
            default -> throw new IllegalStateException("Unexpected policy type: " + policy.type);
        };
        builder.filter((request, next) -> next.exchange(request)
            .flatMap(clientResponse -> Mono.just(clientResponse)
                .filter(response -> clientResponse.statusCode().isError())
                .flatMap(response -> clientResponse.createException())
                .flatMap(Mono::error)
                .thenReturn(clientResponse))
            .retryWhen(retry)
        );
    }

    private Retry linearRetry(RetryPolicy policy) {
        return new LinearRetry(
            policy.maxAttempts,
            policy.initialInterval,
            policy.increment,
            e -> filter(e, policy),
            (retry, retrySignal) -> retrySignal.failure()
        );
    }

    private Retry exponentialRetry(RetryPolicy policy) {
        return Retry.backoff(policy.maxAttempts, Duration.ofMillis(policy.multiplier))
            .filter(e -> filter(e, policy))
            .onRetryExhaustedThrow((retry, retrySignal) -> retrySignal.failure());
    }

    private Retry constantRetry(RetryPolicy policy) {
        return Retry.fixedDelay(policy.maxAttempts, Duration.ofSeconds(policy.backoffPeriod))
            .filter(e -> filter(e, policy))
            .onRetryExhaustedThrow((retry, retrySignal) -> retrySignal.failure());
    }

    private boolean filter(Throwable e, RetryPolicy policy) {
        return e instanceof WebClientResponseException exception
               && policy.retryOnCodes.contains(exception.getStatusCode().value());
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
        private int maxAttempts;
        private int backoffPeriod;
        private int multiplier;
        public List<Integer> retryOnCodes;

        public enum Type {
            NONE, CONSTANT, LINEAR, EXPONENTIAL
        }
    }
}
