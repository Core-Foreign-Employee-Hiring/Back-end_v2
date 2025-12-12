package com.forwork.backend.api.member_specification.dto.external.request;

public record MemberSpecEvaluationExternalRequestDTO(
        String specs,
        String model
) {

    public static MemberSpecEvaluationExternalRequestDTO of(String specs, String model) {
        return new MemberSpecEvaluationExternalRequestDTO(specs, model);
    }

}
