package edu.java.scrapper.interceptor;

import edu.java.scrapper.exception.ApiRequestRateExceededException;
import edu.java.scrapper.ratelimit.RateLimiter;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@AllArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor {
    private final RateLimiter rateLimiter;

    @Override
    public boolean preHandle(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull Object handler
    ) {
        String ip = getIp(request);
        Bucket bucket = rateLimiter.resolveBucket(ip);
        if (bucket.tryConsume(1)) {
            return true;
        } else {
            throw new ApiRequestRateExceededException(ip);
        }
    }

    private String getIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            return forwardedFor.substring(0, forwardedFor.indexOf(','));
        } else {
            return request.getRemoteAddr();
        }
    }
}
