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
        indexes = @Index(name = "idx_major_snapshot_specification_evaluation_id", columnList = "education_snapshot_id")
)
public class MajorSnapshot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "major_snapshot_id")
    private Long id;

    private String major;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "education_snapshot_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EducationSnapshot educationSnapshot;
}
