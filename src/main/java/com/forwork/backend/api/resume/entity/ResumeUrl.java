package com.forwork.backend.api.resume.entity;

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
@Table(name = "resume_url")
public class ResumeUrl extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "resume_url_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Resume resume;

    @Column(nullable = false)
    private String urlTitle;

    @Column(nullable = false)
    private String urlLink;
}
