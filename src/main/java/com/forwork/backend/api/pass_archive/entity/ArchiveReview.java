package com.forwork.backend.api.pass_archive.entity;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ArchiveReview extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="archive_review_id")
    private Long id;

    private double star;
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="writer_id")
    private Member writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="archive_id")
    private PassArchive passArchive;
}
