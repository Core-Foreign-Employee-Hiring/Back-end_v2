package com.forwork.backend.api.member.dto;

import com.forwork.backend.api.member.entity.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberUpdateRequestDTO {

    private String name;
    private String email;
    private String phoneNumber;

    private String zipcode;
    private String address1;
    private String address2;

    private LocalDate birthDate;
    private String nationality;
    private String visa;
    private String education;
    private Gender gender;

    private Boolean termsOfServiceAgreement;
    private Boolean personalInfoAgreement;
    private Boolean adInfoAgreementSmsMms;
    private Boolean adInfoAgreementEmail;
}