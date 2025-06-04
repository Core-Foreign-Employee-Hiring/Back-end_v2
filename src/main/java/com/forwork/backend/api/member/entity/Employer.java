package com.forwork.backend.api.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("EMPLOYER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employer extends Member {

    private String businessRegistrationNumber; // 사업자등록번호
    private String companyName;                // 회사점포명
    private LocalDate establishedDate;         // 설립일
    private JobCategory jobCategory;           // 직종 분류
    private CompanyType companyType;           // 기업 형태

    @Embedded
    private Address companyAddress;

    public Employer(String userId,
                    String password,
                    String name,
                    String email,
                    String phoneNumber,
                    Address address,
                    String businessRegistrationNumber,
                    String companyName,
                    LocalDate establishedDate,
                    LocalDate birthday,
                    Gender gender,
                    CompanyType companyType,
                    JobCategory jobCategory,
                    boolean termsOfServiceAgreement,
                    boolean isOver15,
                    boolean personalInfoAgreement,
                    boolean adInfoAgreementSmsMms,
                    boolean adInfoAgreementEmail) {
        super(userId, password, name, email, phoneNumber, address, Role.EMPLOYER, birthday, gender, termsOfServiceAgreement, isOver15, personalInfoAgreement, adInfoAgreementSmsMms, adInfoAgreementEmail);
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.companyName = companyName;
        this.establishedDate = establishedDate;
        this.companyAddress=address;
        this.jobCategory=jobCategory;
        this.companyType=companyType;
    }

}