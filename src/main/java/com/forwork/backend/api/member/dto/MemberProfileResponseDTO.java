package com.forwork.backend.api.member.dto;

import com.forwork.backend.api.member.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MemberProfileResponseDTO {

    private String name;
    private String email;
    private String phoneNumber;

    private String zipcode;
    private String address1;
    private String address2;

    private LocalDate birthDate;
    private Gender gender;

    private String nationality;
    private String visa;
    private String education;

    private boolean termsOfServiceAgreement;
    private boolean personalInfoAgreement;
    private boolean adInfoAgreementSmsMms;
    private boolean adInfoAgreementEmail;
}