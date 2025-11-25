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
        name="member_career",
        indexes = @Index(name="idx_member_specification_id", columnList = "member_specification_id")
)
public class MemberCareer extends BaseTimeEntity {
    @Id @GeneratedValue(strategy= IDENTITY)
    @Column(name="member_career_id")
    private Long id;

    private String companyName;
    private String position;
    private Integer startYear;
    private Integer startMonth;
    private Integer endYear;
    private Integer endMonth;
    private String contractType;
    @Column(columnDefinition="LONGTEXT")
    private String highlight;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "member_specification_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSpecification memberSpecification;
}
