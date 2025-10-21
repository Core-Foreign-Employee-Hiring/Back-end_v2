package com.forwork.backend.api.recruit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Table(
        name = "recruit_visa_role",
        indexes = {
                @Index(name = "idx_recruit_visa_role", columnList = "recruit_id, visa_id")
        }
)
@Getter
public class RecruitVisa {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="recruit_visa_id")
    private Long id;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="recruit_id")
    private Recruit recruit;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="visa_id")
    private VisaEntity visaEntity;

    public RecruitVisa(Recruit recruit, VisaEntity visaEntity) {
        this.recruit = recruit;
        this.visaEntity = visaEntity;
    }
}
