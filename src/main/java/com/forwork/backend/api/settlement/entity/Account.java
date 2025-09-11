package com.forwork.backend.api.settlement.entity;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String accountName;
    private String bankName;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", unique = true, nullable = false)
    private Member member;

    // 계좌정보 수정 메서드
    public void updateAccount(String accountName, String accountNumber, String bankName) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }
}
