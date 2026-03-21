package com.forwork.backend.api.pay.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public abstract class TossClient {
    protected final RestTemplate tossRestTemplate;

    @Value("${toss.payments.secret-key}")
    protected String secretKey;

    @Value("${toss.payments.base-url}")
    protected String baseUrl;

    protected TossClient(RestTemplate tossRestTemplate) {
        this.tossRestTemplate = tossRestTemplate;
    }
}
