package com.forwork.backend.api.member.dto;

import com.forwork.backend.api.member.entity.Gender;
import com.forwork.backend.api.member.entity.JobRole;
import com.forwork.backend.api.member.entity.Nationality;
import com.forwork.backend.api.member.entity.Visa;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberUpdateRequestDTO {

    private String name;
    private String email;
    private String phoneNumber;

    private String zipcode;
    private String address1;
    private String address2;

    private LocalDate birthDate;
    private Nationality nationality;
    private Visa visa;
    private String education;
    private Gender gender;
    @Size(max=5, message="최대 5개")
    private Set<JobRole> jobRoles;

    private Boolean termsOfServiceAgreement;
    private Boolean personalInfoAgreement;
    private Boolean adInfoAgreementSmsMms;
    private Boolean adInfoAgreementEmail;
}