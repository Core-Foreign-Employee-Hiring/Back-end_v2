package com.forwork.backend.api.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindIdRequestDTO {

    private String name;
    private String phoneNumber;
}
