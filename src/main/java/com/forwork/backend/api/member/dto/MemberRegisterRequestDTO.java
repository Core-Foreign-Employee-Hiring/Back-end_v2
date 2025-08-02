package com.forwork.backend.api.member.dto;

import com.forwork.backend.api.member.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterRequestDTO {

    private String userId;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;

    private String zipcode;
    private String address1;
    private String address2;

    private LocalDate birthDate;
    private Gender gender;

    private String nationality;
    private String education;
    private String visa;

    private boolean termsOfServiceAgreement;
    private boolean isOver15; // 만 15세 이상 확인
    private boolean personalInfoAgreement;
    private boolean adInfoAgreementSmsMms;
    private boolean adInfoAgreementEmail;

}