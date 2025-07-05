package com.forwork.backend.api.recruit.service;

import com.forwork.backend.api.member.entity.JobCategory;
import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.recruit.dto.request.RecruitRequestDTO;
import com.forwork.backend.api.recruit.dto.request.RecruitUpdateRequestDTO;
import com.forwork.backend.api.recruit.dto.response.RecruitDetailResponseDTO;
import com.forwork.backend.api.recruit.dto.response.RecruitDraftResponseDTO;
import com.forwork.backend.api.recruit.dto.response.RecruitPreviewResponseDTO;
import com.forwork.backend.api.recruit.entity.JobCategoryEntity;
import com.forwork.backend.api.recruit.entity.Recruit;
import com.forwork.backend.api.recruit.entity.RecruitBookmark;
import com.forwork.backend.api.recruit.entity.RecruitJobCategory;
import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.api.recruit.enums.RecruitBookmarkStatus;
import com.forwork.backend.api.recruit.repository.JobCategoryEntityRepository;
import com.forwork.backend.api.recruit.repository.RecruitBookmarkRepository;
import com.forwork.backend.api.recruit.repository.RecruitJobCategoryRepository;
import com.forwork.backend.api.recruit.repository.RecruitRepository;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.forwork.backend.common.response.ErrorStatus.RECRUIT_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitService {
    private final RecruitRepository recruitRepository;
    private final JobCategoryEntityRepository jobCategoryEntityJpaRepository;
    private final RecruitJobCategoryRepository recruitJobCategoryJpaRepository;
    private final RecruitReader recruitReader;
    private final RecruitUpdater recruitUpdater;
    private final RecruitBookmarkRepository recruitBookmarkRepository;
    private final MemberRepository memberRepository;


    /*
    * c
    * */

    @Transactional
    public Long save(RecruitRequestDTO recruitRequestDTO) {

        // 공고 저장
        Recruit toRecruit = recruitRequestDTO.toEntity();
        Recruit recruit  = recruitRepository.save(toRecruit);

        // 직종 처리.
        List<JobCategory> jobCategories = recruitRequestDTO.jobCategories();
        List<JobCategoryEntity> allByJobCategories = jobCategoryEntityJpaRepository.findAllByJobCategories(jobCategories);

        List<RecruitJobCategory> recruitJobCategories=new ArrayList<>();

        for (JobCategoryEntity jobCategoryEntity : allByJobCategories) {
            RecruitJobCategory recruitJobCategory = new RecruitJobCategory(recruit, jobCategoryEntity);
            recruitJobCategories.add(recruitJobCategory);
        }

        recruitJobCategoryJpaRepository.saveAll(recruitJobCategories);

        return recruit.getId();
    }


    /*
    * r
    * */

    public RecruitDetailResponseDTO getRecruit(Long memberId, Long recruitId){
        Recruit recruit = recruitReader.getRecruit(recruitId);
        List<RecruitJobCategory> recruitJobCategories = recruit.getRecruitJobCategories();
        List<JobCategory> jobCategories = RecruitUtils.convertToJobCategories(recruitJobCategories);

        RecruitBookmarkStatus recruitBookmarkStatus = getRecruitBookmarkStatus(memberId, recruitId);

        RecruitDetailResponseDTO response = RecruitDetailResponseDTO.fromEntity(recruit, jobCategories, recruitBookmarkStatus);



        return response;
    }

    public RecruitDraftResponseDTO getLatestDraft(Long employerId) {
        Recruit recruit = recruitReader.getLatestDraft(employerId);
        List<RecruitJobCategory> recruitJobCategories = recruit.getRecruitJobCategories();
        List<JobCategory> jobCategories = RecruitUtils.convertToJobCategories(recruitJobCategories);

        RecruitDraftResponseDTO response = RecruitDraftResponseDTO.fromEntity(recruit, jobCategories);

        return response;
    }

    public PageResponseDTO<RecruitPreviewResponseDTO> getRecruits(String keyword, List<JobCategory> jobCategories, List<ContractType> contractTypes,
                                                                  Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page, size);
        Page<Recruit> recruits = recruitReader.getRecruits(keyword, jobCategories, contractTypes, pageable);

        Page<RecruitPreviewResponseDTO> dtos = recruits.map(RecruitPreviewResponseDTO::fromEntity);

        PageResponseDTO<RecruitPreviewResponseDTO> response = PageResponseDTO.of(dtos);

        return response;
    }


    /*
     * u
     * */
    @Transactional
    public void updateRecruit(Long recruitId, RecruitUpdateRequestDTO dto){
        Recruit recruit = recruitReader.getRecruit(recruitId);

        // 공고 수정
        recruitUpdater.update(recruit, dto);

    }

    @Transactional
    public RecruitBookmarkStatus flipRecruitBookmark(Long memberId, Long recruitId){
        Optional<RecruitBookmark> findBookmark = recruitBookmarkRepository.findByRecruitIdAndMemberId(recruitId, memberId);

        if(findBookmark.isPresent()){
            RecruitBookmark recruitBookmark = findBookmark.get();
            recruitBookmarkRepository.delete(recruitBookmark);
            return RecruitBookmarkStatus.INACTIVE;
        }
        else{
            Member member = memberRepository.findById(memberId).get();
            Recruit recruit = recruitRepository.findById(recruitId)
                    .orElseThrow(() -> {
                        log.error("공고 없음. recruitId= {}", recruitId);
                        return new NotFoundException(RECRUIT_NOT_FOUND_EXCEPTION.getMessage());
                    });

            RecruitBookmark recruitBookmark = new RecruitBookmark(recruit, member);
            recruitBookmarkRepository.save(recruitBookmark);
            return RecruitBookmarkStatus.ACTIVE;
        }
    }





    private RecruitBookmarkStatus getRecruitBookmarkStatus(Long memberId, Long recruitId) {
        if(memberId ==null){
            return RecruitBookmarkStatus.INACTIVE;
        }

        Optional<RecruitBookmark> findBookmark = recruitBookmarkRepository.findByRecruitIdAndMemberId(recruitId, memberId);

        if(findBookmark.isPresent()){
            return RecruitBookmarkStatus.ACTIVE;
        }
        else{
            return RecruitBookmarkStatus.INACTIVE;
        }

    }


}
