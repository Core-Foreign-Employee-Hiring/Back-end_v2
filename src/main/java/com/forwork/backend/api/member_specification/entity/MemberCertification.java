package com.forwork.backend.api.member_specification.entity;

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
        name="member_certification",
        indexes = @Index(name="idx_member_specification_id", columnList = "member_specification_id")
)
public class MemberCertification extends BaseTimeEntity {
    @Id @GeneratedValue(strategy= IDENTITY)
    @Column(name="member_certification_id")
    private Long id;

    private String certificationName;
    private Integer acquiredYear;
    private Integer acquiredMonth;
    private String documentUrl;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "member_specification_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemberSpecification memberSpecification;
}
