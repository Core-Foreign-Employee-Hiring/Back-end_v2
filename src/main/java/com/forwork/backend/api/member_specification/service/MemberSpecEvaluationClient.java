package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.external.request.MemberSpecEvaluationExternalRequestDTO;
import com.forwork.backend.api.member_specification.dto.external.response.MemberSpecEvaluationExternalResponseDTO;
import com.forwork.backend.api.member_specification.dto.internal.MemberSpecificationDTO;
import com.forwork.backend.common.exception.InternalServerException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static com.forwork.backend.common.response.ErrorStatus.INTERNAL_SERVER_EXCEPTION;

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
    public MemberSpecEvaluationExternalResponseDTO evaluateSpecification(MemberSpecificationDTO memberSpecification) {

        // 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 바디
        String specsText = MemberSpecTextConverter.toText(memberSpecification);
        MemberSpecEvaluationExternalRequestDTO request = MemberSpecEvaluationExternalRequestDTO.of(specsText, model);

        HttpEntity<MemberSpecEvaluationExternalRequestDTO> entity = new HttpEntity<>(request, headers);

        // 3) AI 서버 호출
        ResponseEntity<MemberSpecEvaluationExternalResponseDTO> response =
                memberSpecRestTemplate.exchange(
                        baseUrl + "/specs/analyze ",
                        HttpMethod.POST,
                        entity,
                        MemberSpecEvaluationExternalResponseDTO.class
                );

        // 4) 응답 반환
        return response.getBody();
    }

    private MemberSpecEvaluationExternalResponseDTO fallbackEvaluateSpecification(MemberSpecificationDTO memberSpecification,
                                                                                  HttpServerErrorException ex) {
        log.warn("[fallbackEvaluateSpecification][HttpServerErrorException][message= {}]", ex.getMessage(), ex);
        throw new InternalServerException(INTERNAL_SERVER_EXCEPTION.getMessage());
    }

    private MemberSpecEvaluationExternalResponseDTO fallbackEvaluateSpecification(MemberSpecificationDTO memberSpecification,
                                                                                  ResourceAccessException ex) {
        log.warn("[fallbackEvaluateSpecification][ResourceAccessException][message= {}]", ex.getMessage(), ex);
        throw new InternalServerException(INTERNAL_SERVER_EXCEPTION.getMessage());
    }

    private MemberSpecEvaluationExternalResponseDTO fallbackEvaluateSpecification(MemberSpecificationDTO memberSpecification,
                                                                                  CallNotPermittedException ex) {
        log.warn("[fallbackEvaluateSpecification][CallNotPermittedException][message= {}]", ex.getMessage(), ex);
        throw new InternalServerException(INTERNAL_SERVER_EXCEPTION.getMessage());
    }

    private MemberSpecEvaluationExternalResponseDTO fallbackEvaluateSpecification(MemberSpecificationDTO memberSpecification,
                                                                                  Throwable ex) {
        log.warn("[fallbackEvaluateSpecification][Throwable][message= {}]", ex.getMessage(), ex);
        throw new InternalServerException(INTERNAL_SERVER_EXCEPTION.getMessage());
    }
}
