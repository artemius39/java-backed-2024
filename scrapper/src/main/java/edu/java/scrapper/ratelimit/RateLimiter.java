package edu.java.scrapper.ratelimit;

import io.github.bucket4j.Bucket;

public interface RateLimiter {
    Bucket resolveBucket(String ip);
}
