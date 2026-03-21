package com.forwork.backend.api.pay.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public abstract class TossClient {
    protected final RestTemplate tossRestTemplate;

    @Value("${toss.payments.secret-key}")
    protected String secretKey;

    @Value("${toss.payments.base-url}")
    protected String baseUrl;

    protected TossClient(RestTemplate tossRestTemplate) {
        this.tossRestTemplate = tossRestTemplate;
    }

    public HttpHeaders createAuthHeaders() {
        String encodedAuth = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);

        return headers;
    }
}
