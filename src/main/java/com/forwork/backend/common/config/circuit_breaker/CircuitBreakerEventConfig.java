package com.forwork.backend.common.config.circuit_breaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CircuitBreakerEventConfig {

    public CircuitBreakerEventConfig(CircuitBreakerRegistry registry) {

        registry.getAllCircuitBreakers().forEach(cb ->
                cb.getEventPublisher()
                        .onStateTransition(event ->
                                log.info("[CircuitBreaker: {}] State change: {} -> {}",
                                        cb.getName(),
                                        event.getStateTransition().getFromState(),
                                        event.getStateTransition().getToState()
                                )
                        )
        );
    }
}
