package com.forwork.backend.api.member.dto;

import com.forwork.backend.api.member.entity.Gender;
import com.forwork.backend.api.member.entity.JobRole;
import com.forwork.backend.api.member.entity.Nationality;
import com.forwork.backend.api.member.entity.Visa;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

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

    private Nationality nationality;
    private Visa visa;
    private String education;

    private List<JobRole> jobRoles;

    private boolean termsOfServiceAgreement;
    private boolean personalInfoAgreement;
    private boolean adInfoAgreementSmsMms;
    private boolean adInfoAgreementEmail;
}