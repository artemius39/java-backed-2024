package edu.java.scrapper.configuration;

import edu.java.scrapper.ratelimit.FixedRateLimiter;
import edu.java.scrapper.ratelimit.RateLimiter;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ratelimit")
@Getter
@Setter
public class RateLimiterConfig {
    private boolean enabled = true;
    private long capacity;
    private long refillRate;
    private long initialTokens;

    @Bean
    public RateLimiter fixedRateLimiter() {
        if (enabled) {
            return new FixedRateLimiter(
                new ConcurrentHashMap<>(),
                capacity,
                refillRate,
                initialTokens
            );
        } else {
            return new FixedRateLimiter(
                new ConcurrentHashMap<>(),
                Long.MAX_VALUE,
                Long.MAX_VALUE,
                Long.MAX_VALUE
            );
        }
    }
}


