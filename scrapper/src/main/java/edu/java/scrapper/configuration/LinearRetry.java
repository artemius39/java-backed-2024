package edu.java.scrapper.configuration;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@AllArgsConstructor
public class LinearRetry extends Retry {
    private final int maxAttempts;
    private final int initialInterval;
    private final int increment;
    private final Predicate<Throwable> filter;
    private final BiFunction<LinearRetry, RetrySignal, Throwable> retryExhaustedGenerator;

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
        return flux.flatMap(rs -> {
            RetrySignal copy = rs.copy();

            if (!filter.test(rs.failure())) {
                return Mono.error(rs.failure());
            }

            if (rs.totalRetries() < maxAttempts) {
                Duration delay = Duration.ofSeconds(initialInterval + rs.totalRetries() * increment);
                return Mono.delay(delay).thenReturn(rs.totalRetries());
            } else {
                return Mono.error(retryExhaustedGenerator.apply(this, copy));
            }
        });
    }
}
