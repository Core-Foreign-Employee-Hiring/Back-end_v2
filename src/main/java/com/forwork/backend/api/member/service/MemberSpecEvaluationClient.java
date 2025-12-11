package com.forwork.backend.api.member.service;

import com.forwork.backend.api.member.dto.MemberSpecEvaluationExternalRequestDTO;
import com.forwork.backend.api.member.dto.MemberSpecEvaluationExternalResponseDTO;
import com.forwork.backend.api.member.dto.MemberSpecificationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
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
                        baseUrl + "/analysis",
                        HttpMethod.POST,
                        entity,
                        MemberSpecEvaluationExternalResponseDTO.class
                );

        // 4) 응답 반환
        return response.getBody();
    }
}
