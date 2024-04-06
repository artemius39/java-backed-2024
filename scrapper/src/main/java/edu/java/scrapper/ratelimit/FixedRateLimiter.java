package edu.java.scrapper.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FixedRateLimiter implements RateLimiter {
    private final Map<String, Bucket> cache;
    private final long capacity;
    private final long refillRate;
    private final long initialTokens;

    @Override
    public Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, this::newBucket);
    }

    private Bucket newBucket(String ip) {
        Bandwidth limit = Bandwidth.builder()
            .capacity(capacity)
            .refillIntervally(refillRate, Duration.ofMinutes(1))
            .initialTokens(initialTokens)
            .build();

        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
}
