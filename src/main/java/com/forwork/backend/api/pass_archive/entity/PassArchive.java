package com.forwork.backend.api.pass_archive.entity;

import com.forwork.backend.api.file.entity.UploadFile;
import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.order.entity.OrderPassArchive;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PassArchive extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passArchiveId;

    private String title;
    private String oneLineReview;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private long price;
    private double star;
    private long starCount;

    private String inquiryUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 썸네일
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_file_id")
    private UploadFile thumbnail;

    // 본문 이미지
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinTable(
            name = "pass_archive_images",
            joinColumns = @JoinColumn(name = "pass_archive_id"),
            inverseJoinColumns = @JoinColumn(name = "upload_file_id")
    )
    @Builder.Default
    private List<UploadFile> images = new ArrayList<>();

    // 상품 파일
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinTable(
            name = "pass_archive_products",
            joinColumns = @JoinColumn(name = "pass_archive_id"),
            inverseJoinColumns = @JoinColumn(name = "upload_file_id")
    )

    @Builder.Default
    private List<UploadFile> products = new ArrayList<>();

    @OneToMany(mappedBy = "passArchive", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<OrderPassArchive> orderPassArchives = new ArrayList<>();

    @OneToMany(mappedBy = "archive", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ArchiveInquiry> archiveInquiries = new ArrayList<>();

    @OneToMany(mappedBy = "passArchive", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ArchiveReview> archiveReviews = new ArrayList<>();
}