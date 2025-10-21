package com.forwork.backend.api.member.dto;

import com.forwork.backend.api.member.entity.Nationality;
import com.forwork.backend.api.member.entity.Gender;
import com.forwork.backend.api.member.entity.JobRole;
import com.forwork.backend.api.member.entity.Visa;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

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

    private Nationality nationality;
    private String education;
    private Visa visa;

    @Size(max=5, message="최대 5개")
    private Set<JobRole> jobRoles;

    private boolean termsOfServiceAgreement;
    private boolean isOver15; // 만 15세 이상 확인
    private boolean personalInfoAgreement;
    private boolean adInfoAgreementSmsMms;
    private boolean adInfoAgreementEmail;

}