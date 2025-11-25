package com.forwork.backend.api.member.entity;

import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Table(
        name="major",
        indexes = @Index(name="idx_member_education_id", columnList = "member_education_id")
)
public class MemberMajor extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_major_id")
    private Long id;

    private String major;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="member_education_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberEducation memberEducation;
}
