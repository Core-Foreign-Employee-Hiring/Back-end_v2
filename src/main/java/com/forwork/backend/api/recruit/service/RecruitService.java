package com.forwork.backend.api.recruit.service;

import com.forwork.backend.api.member.entity.*;
import com.forwork.backend.api.member.repository.JobCategoryEntityRepository;
import com.forwork.backend.api.member.repository.JobRoleEntityRepository;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.recruit.dto.request.RecruitRequestDTO;
import com.forwork.backend.api.recruit.dto.request.RecruitUpdateRequestDTO;
import com.forwork.backend.api.recruit.dto.response.RecruitDetailResponseDTO;
import com.forwork.backend.api.recruit.dto.response.RecruitDraftResponseDTO;
import com.forwork.backend.api.recruit.dto.response.RecruitPreviewResponseDTO;
import com.forwork.backend.api.recruit.entity.*;
import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.api.recruit.enums.LanguageType;
import com.forwork.backend.api.recruit.enums.RecruitBookmarkStatus;
import com.forwork.backend.api.recruit.enums.WorkRegion;
import com.forwork.backend.api.recruit.repository.*;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitService {
    private final RecruitRepository recruitRepository;
    private final JobCategoryEntityRepository jobCategoryEntityJpaRepository;
    private final JobRoleEntityRepository jobRoleEntityRepository;
    private final RecruitJobRoleRepository recruitJobRoleRepository;
    private final LanguageTypeEntityRepository languageTypeEntityRepository;
    private final RecruitLanguageTypeRepository recruitLanguageTypeRepository;
    private final VisaEntityRepository visaEntityRepository;
    private final RecruitVisaRepository recruitVisaRepository;
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
        List<JobCategoryEntity> allByJobCategories =
                jobCategoryEntityJpaRepository.findAllByJobCategories(jobCategories.stream().map(JobCategory::getDbValue).toList());

        Set<RecruitJobCategory> recruitJobCategories=new HashSet<>();

        for (JobCategoryEntity jobCategoryEntity : allByJobCategories) {
            RecruitJobCategory recruitJobCategory = new RecruitJobCategory(recruit, jobCategoryEntity);
            recruitJobCategories.add(recruitJobCategory);
        }

        recruitJobCategoryJpaRepository.saveAll(recruitJobCategories);


        // 직무 처리

        Set<JobRole> jobRoles = recruitRequestDTO.jobRoles();
        List<JobRoleEntity> allByJobRoles =
                jobRoleEntityRepository.findAllByJobRoles(jobRoles.stream().map(JobRole::getDbValue).toList());

        Set<RecruitJobRole> recruitJobRoles=new HashSet<>();

        for (JobRoleEntity jobRoleEntity : allByJobRoles) {
            RecruitJobRole recruitJobRole = new RecruitJobRole(recruit, jobRoleEntity);
            recruitJobRoles.add(recruitJobRole);
        }

        recruitJobRoleRepository.saveAll(recruitJobRoles);

        // 언어 처리

        Set<LanguageType> languageTypes = recruitRequestDTO.languageTypes();
        List<LanguageTypeEntity> allByLanguageTypes =
                languageTypeEntityRepository.findAllByLanguageTypes(languageTypes.stream().map(LanguageType::getDbValue).toList());

        Set<RecruitLanguageType> recruitLanguageTypes=new HashSet<>();

        for (LanguageTypeEntity languageTypeEntity : allByLanguageTypes) {
            RecruitLanguageType recruitLanguageType = new RecruitLanguageType(recruit, languageTypeEntity);
            recruitLanguageTypes.add(recruitLanguageType);
        }

        recruitLanguageTypeRepository.saveAll(recruitLanguageTypes);

        // 비자 처리

        Set<Visa> visas = recruitRequestDTO.visas();
        List<VisaEntity> allByVisas =
                visaEntityRepository.findAllByVisas(visas.stream().map(Visa::getDbValue).toList());

        Set<RecruitVisa> recruitVisas=new HashSet<>();

        for (VisaEntity visaEntity : allByVisas) {
            RecruitVisa recruitVisa = new RecruitVisa(recruit, visaEntity);
            recruitVisas.add(recruitVisa);
        }

        recruitVisaRepository.saveAll(recruitVisas);

        return recruit.getId();
    }


    /*
    * r
    * */

    public RecruitDetailResponseDTO getRecruit(Long memberId, Long recruitId){
        Recruit recruit = recruitReader.getRecruit(recruitId);
        // 직종
        Set<RecruitJobCategory> recruitJobCategories = recruit.getRecruitJobCategories();
        List<JobCategory> jobCategories = RecruitUtils.convertToJobCategories(recruitJobCategories);

        // 직무
        Set<RecruitJobRole> recruitJobRoles = recruit.getRecruitJobRoles();
        List<JobRole> jobRoles = JobRole.convertToJobRolesByRecruit(recruitJobRoles);

        // 언어
        Set<RecruitLanguageType> recruitLanguageTypes = recruit.getRecruitLanguageTypes();
        List<LanguageType> languageTypes = RecruitUtils.convertToLanguageTypes(recruitLanguageTypes);

        // 비자
        Set<RecruitVisa> recruitVisas = recruit.getRecruitVisas();
        List<Visa> visas = RecruitUtils.convertToVisas(recruitVisas);

        RecruitBookmarkStatus recruitBookmarkStatus = getRecruitBookmarkStatus(memberId, recruitId);
        RecruitDetailResponseDTO response = RecruitDetailResponseDTO.fromEntity(recruit, jobCategories, recruitBookmarkStatus, jobRoles, languageTypes, visas);

        return response;
    }

    public RecruitDraftResponseDTO getLatestDraft(Long employerId) {
        Recruit recruit = recruitReader.getLatestDraft(employerId);
        Set<RecruitJobCategory> recruitJobCategories = recruit.getRecruitJobCategories();
        List<JobCategory> jobCategories = RecruitUtils.convertToJobCategories(recruitJobCategories);

        RecruitDraftResponseDTO response = RecruitDraftResponseDTO.fromEntity(recruit, jobCategories);

        return response;
    }

    public PageResponseDTO<RecruitPreviewResponseDTO> getRecruits(String keyword, Integer page, Integer size,
                                                                  Set<JobRole> jobRoles, Nationality nationality, Set<LanguageType> languageTypes, Set<Visa> visas, Set<WorkRegion> workRegions, ContractType contractType) {

        validateRecruitSelectionLimits(jobRoles, languageTypes, workRegions);


        Pageable pageable= PageRequest.of(page, size);
        Page<Recruit> recruits = recruitReader.getRecruits(keyword, pageable, jobRoles, nationality, languageTypes, visas, workRegions, contractType);

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


    /*
    * d
    * */
    @Transactional
    public void deleteRecruit(Long recruitId){
        // 카테고리 삭제
        recruitJobCategoryJpaRepository.deleteByRecruitId(recruitId);

        // 직무 삭제
         recruitJobRoleRepository.deleteByRecruitId(recruitId);

        // 언어 삭제
        recruitLanguageTypeRepository.deleteByRecruitId(recruitId);

        // 비자 삭제
        recruitVisaRepository.deleteByRecruitId(recruitId);

        // 북마크 삭제
        recruitBookmarkRepository.deleteByRecruitId(recruitId);

        // 공고 삭제
        recruitRepository.deleteById(recruitId);
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

    private static void validateRecruitSelectionLimits(Set<JobRole> jobRoles, Set<LanguageType> languageTypes, Set<WorkRegion> workRegions) {
        if (jobRoles != null && jobRoles.size() > 5) {
            throw new BadRequestException(RECRUIT_JOB_ROLE_LIMIT_EXCEEDED.getMessage());
        }

        if (languageTypes != null && languageTypes.size() > 5) {
            throw new BadRequestException(RECRUIT_LANGUAGE_TYPE_LIMIT_EXCEEDED.getMessage());
        }

        if (workRegions != null && workRegions.size() > 3) {
            throw new BadRequestException(RECRUIT_WORK_REGION_LIMIT_EXCEEDED.getMessage());
        }
    }


}
