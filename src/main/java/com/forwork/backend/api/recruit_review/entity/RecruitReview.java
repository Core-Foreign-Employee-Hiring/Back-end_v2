package com.forwork.backend.api.recruit_review.entity;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewUpdateDTO;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class RecruitReview extends BaseTimeEntity {
    @Id@GeneratedValue(strategy = IDENTITY)
    @Column(name="recruit_review_id")
    private Long id;

    private String title;
    private String content;

    @Enumerated(STRING)
    private JobCategory jobCategory;

    private String region1;
    private String region2;

    private long readCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    private boolean isDeleted;


    public void update(RecruitReviewUpdateDTO dto){
        this.title= dto.title();
        this.content= dto.content();
        this.jobCategory= dto.jobCategory();
        this.region1= dto.region1();
        this.region2= dto.region2();
    }

    public void delete(){
        isDeleted = true;
    }
}
