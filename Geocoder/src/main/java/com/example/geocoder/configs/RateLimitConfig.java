package com.example.geocoder.configs;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

@Configuration
public class RateLimitConfig {
    
    @Bean
    public RateLimiterRegistry rateLimiterRegistry(){
        RateLimiterConfig config = RateLimiterConfig.custom()
            .limitForPeriod(50)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofMillis(10))
            .build();
        return RateLimiterRegistry.of(config);
    }
}
