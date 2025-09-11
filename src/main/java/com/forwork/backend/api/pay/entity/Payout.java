package com.forwork.backend.api.pay.entity;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.pay.enums.PayoutStatus;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payout extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payout_id")
    private Long id;

    private String totalAmount;
    @Enumerated(STRING)
    private PayoutStatus payoutStatus;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private Member seller;
}
