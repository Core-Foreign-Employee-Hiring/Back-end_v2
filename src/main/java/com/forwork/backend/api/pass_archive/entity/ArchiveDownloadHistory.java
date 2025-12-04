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

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Table(
        name = "archive_download_history",
        indexes = {
                @Index(name = "idx_buyer_id", columnList = "buyer_id"),
                @Index(name = "idx_archive_id", columnList = "archive_id")
        }
)
public class ArchiveDownloadHistory extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="archive_download_history_id")
    private Long id;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "buyer_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member buyer;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name = "archive_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private PassArchive passArchive;
}
