package com.forwork.backend.api.recruit_review.service;

import com.forwork.backend.api.recruit_review.dto.query.RecruitReviewChildCommentStatQueryDTO;
import com.forwork.backend.api.recruit_review.entity.RecruitReviewComment;
import com.forwork.backend.api.recruit_review.repository.RecruitReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecruitReviewCommentDeleter {
    private final RecruitReviewCommentRepository recruitReviewCommentRepository;


    /**
     * if 댓글
     *  if 대댓글 있으면
     *      if 대댓글 전부 삭제 상태
     *          : 바로 삭제
     *      else 대댓글 남아 있음.
     *          : 삭제 making
     *  else 대댓글 없으면
     *      : 바로 삭제
     * else 대댓글
     *      : 바로 삭제
     */
    public void deleteRecruitReviewComment(RecruitReviewComment comment) {
        // 댓글이면
        if(comment.getParent()==null){

            RecruitReviewChildCommentStatQueryDTO childCommentStats = recruitReviewCommentRepository.findChildCommentStatsByParentId(comment.getId());

            if(childCommentStats.childCount()!=0){ // 대댓글이 있으면
                if(childCommentStats.childCount()==childCommentStats.deletedCount()){ // 대댓글 전부 삭제 상태면
                    comment.delete();
                }
                else{ // 대댓글 남아 있으면.
                    comment.deleteButHasChild();
                }
            }
            else{ // 대댓글 없으면
                comment.delete();
            }
        }
        else{ // 대댓글이면
            comment.delete();
        }
    }
}
