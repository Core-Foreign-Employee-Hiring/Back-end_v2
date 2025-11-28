package com.forwork.backend.api.member.service;

import com.forwork.backend.api.member.dto.MemberSpecEvaluationExternalRequestDTO;
import com.forwork.backend.api.member.dto.MemberSpecEvaluationExternalResponseDTO;
import com.forwork.backend.api.member.dto.MemberSpecificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberSpecEvaluationClient {

    public MemberSpecEvaluationExternalResponseDTO evaluateSpecification(MemberSpecificationDTO memberSpecification){

        MemberSpecEvaluationExternalRequestDTO request = MemberSpecEvaluationExternalRequestDTO.of(memberSpecification);

        /*
        * 여기서 ai 서버 호출해서 얻어온다.
        * */

        MemberSpecEvaluationExternalResponseDTO response = MemberSpecEvaluationExternalResponseDTO.mockEvaluationResponse();

        return response;
    }

}
