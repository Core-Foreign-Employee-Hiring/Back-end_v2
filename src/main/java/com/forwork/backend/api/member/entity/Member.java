package com.forwork.backend.api.member.entity;

import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;             // 엔티티 식별번호

    @Column(unique = true)
    private String userId;       // 아이디
    private String password;     // 비밀번호
    private String name;         // 이름
    @Column(unique = true)
    private String email;        // 이메일
    @Column(unique = true)
    private String phoneNumber;  // 전화번호
    private String refreshToken; // 리프레시토큰
    private LocalDate birthday;  // 생년월일
    private Gender gender;       // 성별

    private String nationality; // 국적
    private String education;   // 학력
    private String visa;        // 비자

    @Enumerated(EnumType.STRING)
    private Role role;           // 회원 Role (USER, ADMIN 등)

    @Embedded
    private Address address;     // 주소

    private String profileImage; // 프로필이미지

    private boolean termsOfServiceAgreement;
    private boolean isOver15; // 만 15세 이상 확인
    private boolean personalInfoAgreement;
    private boolean adInfoAgreementSmsMms;
    private boolean adInfoAgreementEmail;

    // 리프레시토큰 업데이트
    public Member updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Member updatePassword(String encodedPassword) {
        this.password = encodedPassword;
        return this;
    }

}