package com.forwork.backend.api.recruit_review.entity;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewCommentUpdateDTO;
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
        name = "recruit_review_comment",
        indexes = {
                @Index(name = "idx_is_deleted_review_id", columnList = "is_deleted, recruit_review_id")
        }
)
public class RecruitReviewComment extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="recruit_review_comment_id")
    private Long id;

    private String comment;

    @ManyToOne(fetch =LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch =LAZY)
    @JoinColumn(name = "recruit_review_id")
    private RecruitReview recruitReview;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="parent_id")
    private RecruitReviewComment parent;

    private boolean isDeleted;
    private boolean isDeletedButHasChild;


    public void delete(){
        this.comment = "삭제된 댓글입니다.";
        isDeleted = true;
    }

    public void deleteButHasChild(){
        this.comment = "삭제된 댓글입니다.";
        this.isDeletedButHasChild = true;
    }

    public void update(RecruitReviewCommentUpdateDTO dto){
        this.comment=dto.comment();
    }
}
