package com.forwork.backend.api.recruit_review.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewCommentCreateDTO;
import com.forwork.backend.api.recruit_review.dto.request.RecruitReviewCommentUpdateDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewChildCommentResponseDTO;
import com.forwork.backend.api.recruit_review.dto.response.RecruitReviewParentCommentResponseDTO;
import com.forwork.backend.api.recruit_review.entity.RecruitReview;
import com.forwork.backend.api.recruit_review.entity.RecruitReviewComment;
import com.forwork.backend.api.recruit_review.repository.RecruitReviewCommentRepository;
import com.forwork.backend.api.recruit_review.repository.RecruitReviewRepository;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitReviewCommentService {
    private final RecruitReviewCommentRepository reviewCommentRepository;
    private final MemberRepository memberRepository;
    private final RecruitReviewRepository reviewRepository;
    private final RecruitReviewCommentDeleter recruitReviewCommentDeleter;


    /*
    * c
    * */

    /**
     * @apiNote (대)댓글 생성.
     */
    @Transactional
    public void createRecruitReviewComment(Long writerId, Long recruitReviewId, RecruitReviewCommentCreateDTO dto) {    
        // 부모 댓글 조회
        RecruitReviewComment parentComment = getParentComment(dto.parentId());
        
        // writer 조회
        Member writer = memberRepository.findById(writerId)
                .orElseThrow(() -> {
                    log.warn("[createRecruitReviewComment][member is not found][writerId= {}]", writerId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });
        
        // 채용 후기 조회
        RecruitReview recruitReview = reviewRepository.findById(recruitReviewId)
                .orElseThrow(() -> {
                    log.warn("[createRecruitReviewComment][recruitReview is not found][recruitReviewId= {}]", recruitReviewId);
                    return new NotFoundException(RECRUIT_REVIEW_NOT_FOUND_EXCEPTION.getMessage());
                });


        RecruitReviewComment recruitReviewComment = dto.toEntity(writer, parentComment, recruitReview);

        reviewCommentRepository.save(recruitReviewComment);
    }



    /*
    * r
    * */

    /**
     * @apiNote 특정 후기의 댓글들 조회.
     */
    public List<RecruitReviewParentCommentResponseDTO> getComments(Long memberId, Long recruitReviewId) {
        List<RecruitReviewComment> comments = reviewCommentRepository.findRecruitReviewCommentsByRecruitReviewId(recruitReviewId);

        // 부모 댓글
        List<RecruitReviewComment> parentComments = comments.stream()
                .filter(c -> c.getParent() == null)
                .collect(Collectors.toList());


        // 자식 댓글
        List<RecruitReviewComment> childComments = comments.stream()
                .filter(c -> c.getParent() != null)
                .collect(Collectors.toList());


        // 자식 댓글들 부모 찾아줌

        // key: parentId, value: childComments
        Map<Long, List<RecruitReviewChildCommentResponseDTO>> map=new HashMap<>();

        // 형제끼리 모임
        for (RecruitReviewComment childComment : childComments) {
            Long parentId=childComment.getParent().getId();

            map.computeIfAbsent(parentId, k -> new ArrayList<>())
                    .add(RecruitReviewChildCommentResponseDTO.of(childComment, memberId));
        }

        // 부모 찾아줌
        List<RecruitReviewParentCommentResponseDTO> response = parentComments.stream()
                .map(p -> RecruitReviewParentCommentResponseDTO.of(p, memberId, map.get(p.getId())))
                .collect(Collectors.toList());

        return response;
    }

    /*
    * u
    * */

    /**
     * @apiNote 채용 후기 댓글 수정
     */

    @Transactional
    public void updateRecruitReviewComment(Long memberId, Long commentId, RecruitReviewCommentUpdateDTO dto) {
        RecruitReviewComment recruitReviewComment = reviewCommentRepository.findByIdWithWriterAndParent(commentId)
                .orElseThrow(() -> {
                    log.warn("[updateRecruitReviewComment][comment is not found][commentId= {}]", commentId);
                    return new NotFoundException(COMMENT_NOT_FOUND_EXCEPTION.getMessage());
                });

        validateRecruitReviewCommentOwnership(memberId, recruitReviewComment);

        recruitReviewComment.update(dto);
    }

    /*
    * d
    * */

    /**
     * @apiNote 댓글 삭제
     */

    @Transactional
    public void deleteRecruitReviewComment(Long memberId, Long commentId){
        RecruitReviewComment recruitReviewComment = reviewCommentRepository.findByIdWithWriterAndParent(commentId)
                .orElseThrow(() -> {
                    log.warn("[deleteRecruitReviewComment][comment is not found][commentId= {}]", commentId);
                    return new NotFoundException(COMMENT_NOT_FOUND_EXCEPTION.getMessage());
                });

        validateRecruitReviewCommentOwnership(memberId, recruitReviewComment);

        recruitReviewCommentDeleter.deleteRecruitReviewComment(recruitReviewComment);

    }


    private void validateRecruitReviewCommentOwnership(Long employerId, RecruitReviewComment recruitReviewComment) {
        if (!recruitReviewComment.getWriter().getId().equals(employerId)) {
            log.warn("[validateRecruitReviewCommentOwnership][소유자가 아님.][소유자= {}, 지금 사용자= {}]", recruitReviewComment.getWriter().getId(), employerId);
            throw new BadRequestException(RECRUIT_REVIEW_COMMENT_OWNER_FORBIDDEN_EXCEPTION.getMessage());
        }
    }


    private RecruitReviewComment getParentComment(Long parentId) {
        // 작성 댓글이 부모인 경우 -> null
        if(parentId==null){
            return null;
        }

        RecruitReviewComment parentComment = reviewCommentRepository.findByIdWithParent(parentId)
                .orElseThrow(() -> {
                    log.warn("[getParentComment][parent comment is not found][parentId= {}]", parentId);
                    return new NotFoundException(PARENT_COMMENT_NOT_FOUND_EXCEPTION.getMessage());
                });


        // parentId 가 진짜 부모
        if(parentComment.getParent()==null){
            return parentComment;
        }
        else{ // 사실 형제 Id
            return parentComment.getParent();
        }
    }
}
