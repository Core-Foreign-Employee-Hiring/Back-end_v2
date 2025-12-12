package com.forwork.backend.common.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean(name="tossRestTemplate")
    public RestTemplate tossRestTemplate() {
        return buildRestTemplate(5000, 15000);
    }

    @Bean(name = "memberSpecRestTemplate")
    public RestTemplate memberSpecRestTemplate() {
        return buildRestTemplate(10000, 20000);
    }

    private RestTemplate buildRestTemplate(int connectTimeout, int readTimeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}

