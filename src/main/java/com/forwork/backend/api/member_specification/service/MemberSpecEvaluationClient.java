package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.external.request.MemberSpecEvaluationExternalRequestDTO;
import com.forwork.backend.api.member_specification.dto.external.response.MemberSpecEvaluationExternalResponseDTO;
import com.forwork.backend.api.member_specification.dto.internal.MemberSpecEvaluationInternalDTO;
import com.forwork.backend.api.member_specification.dto.internal.MemberSpecificationDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class MemberSpecEvaluationClient {
    private final RestTemplate memberSpecRestTemplate;
    @Value("${spec.base-url}")
    private String baseUrl;
    @Value("${spec.analysis.model}")
    private String model;

    public MemberSpecEvaluationClient(@Qualifier("memberSpecRestTemplate") RestTemplate memberSpecRestTemplate) {
        this.memberSpecRestTemplate = memberSpecRestTemplate;
    }

    @CircuitBreaker(name = "specEvaluation", fallbackMethod = "fallbackEvaluateSpecification")
    public MemberSpecEvaluationInternalDTO evaluateSpecification(MemberSpecificationDTO memberSpecification) {

        // 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 바디
        String specsText = MemberSpecTextConverter.toText(memberSpecification);
        MemberSpecEvaluationExternalRequestDTO request = MemberSpecEvaluationExternalRequestDTO.of(specsText, model);

        HttpEntity<MemberSpecEvaluationExternalRequestDTO> entity = new HttpEntity<>(request, headers);

        // 3) AI 서버 호출
        ResponseEntity<MemberSpecEvaluationExternalResponseDTO> dto =
                memberSpecRestTemplate.exchange(
                        baseUrl + "/analysis",
                        HttpMethod.POST,
                        entity,
                        MemberSpecEvaluationExternalResponseDTO.class
                );

        // 4) 응답 반환

        MemberSpecEvaluationInternalDTO response = MemberSpecEvaluationInternalDTO.success(dto.getBody());

        return response;
    }

    private MemberSpecEvaluationInternalDTO fallbackEvaluateSpecification(HttpClientErrorException e) {
        log.warn("[fallbackEvaluateSpecification][HttpClientErrorException][message= {}]", e.getMessage(), e);
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());

        return MemberSpecEvaluationInternalDTO.failure(status, "");
    }

    private MemberSpecEvaluationInternalDTO fallbackEvaluateSpecification(HttpServerErrorException e) {
        log.warn("[fallbackEvaluateSpecification][HttpServerErrorException][message= {}]", e.getMessage(), e);
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());

        return MemberSpecEvaluationInternalDTO.failure(status, "");
    }

    private MemberSpecEvaluationInternalDTO fallbackEvaluateSpecification(ResourceAccessException e) {
        log.warn("[fallbackEvaluateSpecification][ResourceAccessException][message= {}]", e.getMessage(), e);

        return MemberSpecEvaluationInternalDTO.failure(HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    private MemberSpecEvaluationInternalDTO fallbackEvaluateSpecification(CallNotPermittedException e) {
        log.warn("[fallbackEvaluateSpecification][CallNotPermittedException][message= {}]", e.getMessage(), e);

        return MemberSpecEvaluationInternalDTO.failure(HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    private MemberSpecEvaluationInternalDTO fallbackEvaluateSpecification(Throwable e) {
        log.warn("[fallbackEvaluateSpecification][Throwable][message= {}]", e.getMessage(), e);

        return MemberSpecEvaluationInternalDTO.failure(HttpStatus.INTERNAL_SERVER_ERROR, "");
    }
}
