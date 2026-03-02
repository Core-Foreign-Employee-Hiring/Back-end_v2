package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.external.response.MemberSpecEvaluationExternalResponseDTO;
import com.forwork.backend.api.member_specification.dto.internal.MemberSpecificationDTO;
import com.forwork.backend.api.member_specification.dto.projection.SpecificationEvaluationRankProjection;
import com.forwork.backend.api.member_specification.dto.request.*;
import com.forwork.backend.api.member_specification.dto.response.MemberSpecEvaluationResponseDTO;
import com.forwork.backend.api.member_specification.dto.response.MemberSpecificationResponseDTO;
import com.forwork.backend.api.member_specification.dto.response.SpecEvaluationPageResponse;
import com.forwork.backend.api.member_specification.entity.MemberSpecification;
import com.forwork.backend.api.member_specification.entity.SpecificationEvaluation;
import com.forwork.backend.api.member_specification.repository.*;
import com.forwork.backend.common.dto.PageResponseDTO;
import com.forwork.backend.common.exception.NotFoundException;
import com.forwork.backend.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSpecificationFacade {
    private final MemberSpecificationRepository memberSpecificationRepository;
    private final MemberEducationRepository memberEducationRepository;
    private final MemberMajorRepository memberMajorRepository;
    private final MemberLanguageSkillRepository memberLanguageSkillRepository;
    private final MemberCertificationRepository memberCertificationRepository;
    private final MemberCareerRepository memberCareerRepository;
    private final MemberAwardRepository memberAwardRepository;
    private final MemberExperienceRepository memberExperienceRepository;
    private final MemberSpecificationReader memberSpecificationReader;
    private final MemberSpecEvaluationClient memberSpecEvaluationClient;
    private final SpecificationEvaluationRepository specificationEvaluationRepository;
    private final MemberEducationCreator memberEducationCreator;
    private final MemberLanguageSkillCreator memberLanguageSkillCreator;
    private final MemberCertificationCreator memberCertificationCreator;
    private final MemberCareerCreator memberCareerCreator;
    private final MemberAwardCreator memberAwardCreator;
    private final MemberExperienceCreator memberExperienceCreator;
    private final MemberLanguageSkillDeleter memberLanguageSkillDeleter;
    private final MemberCertificationDeleter memberCertificationDeleter;
    private final MemberCareerDeleter memberCareerDeleter;
    private final MemberAwardDeleter memberAwardDeleter;
    private final MemberExperienceDeleter memberExperienceDeleter;
    private final MemberEducationDeleter memberEducationDeleter;
    private final MemberSpecificationService memberSpecificationService;




    /*
     * c
     * */

    /**
     * 학력
     */
    @Transactional
    public void createEducation(Long memberId, EducationRequestDTO educationRequestDTO) {

        memberSpecificationRepository.insertIgnore(memberId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[createEducation][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberEducationCreator.create(memberSpec, educationRequestDTO);

    }

    /**
     * 어학
     */
    @Transactional
    public void createLanguageSkill(Long memberId, LanguageSkillRequestDTO languageSkillRequestDTO) {

        memberSpecificationRepository.insertIgnore(memberId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[createLanguageSkill][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberLanguageSkillCreator.create(memberSpec, languageSkillRequestDTO);

    }


    /**
     * 자격증
     */
    @Transactional
    public void createCertification(Long memberId, CertificationRequestDTO certificationRequestDTO) {

        memberSpecificationRepository.insertIgnore(memberId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[createCertification][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberCertificationCreator.create(memberSpec, certificationRequestDTO);

    }

    /**
     * 경력사항
     */
    @Transactional
    public void createCareer(Long memberId, CareerRequestDTO careerRequestDTO) {

        memberSpecificationRepository.insertIgnore(memberId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[createCareer][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberCareerCreator.create(memberSpec, careerRequestDTO);
    }

    /**
     * 수상
     */
    @Transactional
    public void createAward(Long memberId, AwardCreateDTO awardCreateDTO) {

        memberSpecificationRepository.insertIgnore(memberId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[createAward][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberAwardCreator.create(memberSpec, awardCreateDTO);
    }

    /**
     * 경험
     */
    @Transactional
    public void createExperience(Long memberId, ExperienceRequestDTO experienceRequestDTO) {

        memberSpecificationRepository.insertIgnore(memberId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[createExperience][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberExperienceCreator.create(memberSpec, experienceRequestDTO);
    }

    /**
     * 스펙 평가
     */

    public Long evaluateSpecification(Long memberId, SpecificationEvaluationRequestDTO request) {
        MemberSpecificationDTO memberSpecificationDTO = memberSpecificationReader.getMemberSpecification(memberId);

        // ai 서버에거 갖고 온다.
        MemberSpecEvaluationExternalResponseDTO memberSpecEvaluationExternalResponseDTO = memberSpecEvaluationClient.evaluateSpecification(memberSpecificationDTO);

        Long id = memberSpecificationService.createEvaluationWithSnapshots(memberSpecificationDTO, request.specName(), memberSpecEvaluationExternalResponseDTO);


        return id;
    }

    /*
     * r
     * */

    /**
     * 입력한 스펙 조회
     */

    public MemberSpecificationResponseDTO getMemberSpecification(Long memberId) {
        MemberSpecificationDTO memberSpecification = memberSpecificationReader.getMemberSpecification(memberId);

        MemberSpecificationResponseDTO response = MemberSpecificationResponseDTO.of(memberSpecification);
        return response;
    }

    /**
     * 스펙 평가 조회
     */
    public MemberSpecEvaluationResponseDTO getSpecEvaluation(Long memberId, Long specEvaluationId) {

        // 스펙 조회
        MemberSpecification memberSpecification = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[getSpecEvaluation][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        // 스펙 평가 조회
        SpecificationEvaluation specificationEvaluation = specificationEvaluationRepository.findBySpecificationEvaluationIdWithSpec(specEvaluationId)
                .orElseThrow(() -> {
                    log.warn("[getSpecEvaluation][스펙 평가 없음.][specEvaluationId= {}]", specEvaluationId);
                    return new NotFoundException(SPEC_EVALUATION_NOT_FOUND_EXCEPTION.getMessage());
                });

        // 본인 거 맞나?

        if (!memberSpecification.getId().equals(specificationEvaluation.getMemberSpecification().getId())) {
            log.warn("[getSpecEvaluation][본인 거 아님][본인 스펙 id= {}, 스펙 평가 id= {}]", memberSpecification.getId(), specEvaluationId);

            throw new UnauthorizedException(SPEC_EVALUATION_NOT_OWNER_EXCEPTION.getMessage());
        }

        /*
         * 상위 몇 퍼?
         * */

        // 전체 개수
        long totalCount = specificationEvaluationRepository.count();

        // 나보다 큰 거 몇 개?
        long higherThan = specificationEvaluationRepository.countHigherThan(specificationEvaluation.getScore());
        double topPercent = 100.0 * ((double) higherThan / totalCount);

        MemberSpecEvaluationResponseDTO response = MemberSpecEvaluationResponseDTO.of(specificationEvaluation, topPercent);

        return response;
    }

    /**
     * 스펙 평가 리스트 조회
     */
    public PageResponseDTO<SpecEvaluationPageResponse> getSpecEvaluations(Long memberId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<SpecificationEvaluation> specs = specificationEvaluationRepository.findSpecByMemberId(memberId, pageable);


        /*
         * 상위 몇 퍼?
         * */

        List<Integer> scores = specs.getContent().stream().map(SpecificationEvaluation::getScore).toList();

        List<SpecificationEvaluationRankProjection> ranksWithPercent = specificationEvaluationRepository.countHigherThan(scores);

        // 로그 찍기
        ranksWithPercent.forEach(p ->
                log.info("score={}, count={}", p.getScore(), p.getCount())
        );

        long totalCount = specificationEvaluationRepository.count();


        Map<Integer, Double> topPercentMap = ranksWithPercent.stream()
                .collect(Collectors.toMap(
                        SpecificationEvaluationRankProjection::getScore,
                        p -> 100.0 * ((double) (p.getCount()) / totalCount)
                ));


        Page<SpecEvaluationPageResponse> map = specs.map(p -> SpecEvaluationPageResponse.of(p, topPercentMap.get(p.getScore())));

        PageResponseDTO<SpecEvaluationPageResponse> response = PageResponseDTO.of(map);

        return response;
    }


    /*
     * u
     * */

    /**
     * 학력
     */
    @Transactional
    public void updateEducation(Long memberId, Long educationId, EducationRequestDTO educationRequestDTO) {
        memberEducationDeleter.delete(memberId, educationId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[updateEducation][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });
        memberEducationCreator.create(memberSpec, educationRequestDTO);

    }

    /**
     * 어학
     */
    @Transactional
    public void updateLanguageSkill(Long memberId, Long languageSillId, LanguageSkillRequestDTO.LanguageSkill languageSkillRequestDTO) {

        memberLanguageSkillDeleter.delete(memberId, languageSillId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[updateLanguageSkill][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberLanguageSkillCreator.create(memberSpec, languageSkillRequestDTO);

    }


    /**
     * 자격증
     */
    @Transactional
    public void updateCertification(Long memberId, Long certificationId, CertificationRequestDTO.Certification certificationRequestDTO) {

        memberCertificationDeleter.delete(memberId, certificationId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[updateCertification][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberCertificationCreator.create(memberSpec, certificationRequestDTO);

    }

    /**
     * 경력사항
     */
    @Transactional
    public void updateCareer(Long memberId, Long careerId, CareerRequestDTO.Career careerRequestDTO) {

        memberCareerDeleter.delete(memberId, careerId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[updateCareer][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberCareerCreator.create(memberSpec, careerRequestDTO);
    }

    /**
     * 수상
     */
    @Transactional
    public void updateAward(Long memberId, Long awardId, AwardCreateDTO.Award awardCreateDTO) {

        memberAwardDeleter.delete(memberId, awardId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[updateAward][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberAwardCreator.create(memberSpec, awardCreateDTO);
    }

    /**
     * 경험
     */
    @Transactional
    public void updateExperience(Long memberId, Long experienceId, ExperienceRequestDTO.Experience experienceRequestDTO) {

        memberExperienceDeleter.delete(memberId, experienceId);

        MemberSpecification memberSpec = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[updateExperience][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        memberExperienceCreator.create(memberSpec, experienceRequestDTO);
    }



    /*
     * d
     * */


    /**
     * 스펙, 평가 삭제
     */

    @Transactional
    public void deleteMemberSpecification(Long memberId) {

        MemberSpecification memberSpecification = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[deleteMemberSpecification][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        Long memberSpecificationId = memberSpecification.getId();


        /*
         * 평가
         * */

        specificationEvaluationRepository.deleteByMemberSpecificationId(memberSpecificationId);

        /*
         * 경험
         * */

        memberExperienceRepository.deleteByMemberSpecificationId(memberSpecificationId);

        /*
         * 수상
         * */

        memberAwardRepository.deleteByMemberSpecificationId(memberSpecificationId);

        /*
         * 경력
         * */

        memberCareerRepository.deleteByMemberSpecificationId(memberSpecificationId);

        /*
         * 자격증 -> 이상함
         * */

        memberCertificationRepository.deleteByMemberSpecificationId(memberSpecificationId);

        /*
         * 어학
         * */

        memberLanguageSkillRepository.deleteByMemberSpecificationId(memberSpecificationId);


        /*
         * 학력
         * */

        memberEducationRepository.findByMemberSpecificationId(memberSpecificationId)
                .ifPresent((education) -> {
                    Long educationId = education.getId();

                    memberMajorRepository.deleteByMemberEducationId(educationId);


                    memberEducationRepository.deleteById(educationId);

                });


        /*
         * 스펙
         * */

        memberSpecificationRepository.delete(memberSpecification);

    }

    /**
     * 학력
     */
    @Transactional
    public void deleteEducation(Long memberId, IdsDeleteRequestDTO requestDTO) {

        List<Long> ids = Optional.ofNullable(requestDTO.ids()).orElse(List.of());

        for (Long id : ids) {
            memberEducationDeleter.delete(memberId, id);
        }

    }

    /**
     * 어학
     */
    @Transactional
    public void deleteLanguageSkill(Long memberId, IdsDeleteRequestDTO requestDTO) {

        List<Long> ids = Optional.ofNullable(requestDTO.ids()).orElse(List.of());


        for (Long id : ids) {
            memberLanguageSkillDeleter.delete(memberId, id);
        }

    }


    /**
     * 자격증
     */
    @Transactional
    public void deleteCertification(Long memberId, IdsDeleteRequestDTO requestDTO) {

        List<Long> ids = Optional.ofNullable(requestDTO.ids()).orElse(List.of());


        for (Long id : ids) {
            memberCertificationDeleter.delete(memberId, id);
        }

    }


    /**
     * 경력사항
     */
    @Transactional
    public void deleteCareer(Long memberId, IdsDeleteRequestDTO requestDTO) {

        List<Long> ids = Optional.ofNullable(requestDTO.ids()).orElse(List.of());


        for (Long id : ids) {
            memberCareerDeleter.delete(memberId, id);
        }

    }


    /**
     * 수상
     */
    @Transactional
    public void deleteAward(Long memberId, IdsDeleteRequestDTO requestDTO) {

        List<Long> ids = Optional.ofNullable(requestDTO.ids()).orElse(List.of());


        for (Long id : ids) {
            memberAwardDeleter.delete(memberId, id);
        }

    }


    /**
     * 경험
     */
    @Transactional
    public void deleteExperience(Long memberId, IdsDeleteRequestDTO requestDTO) {

        List<Long> ids = Optional.ofNullable(requestDTO.ids()).orElse(List.of());

        for (Long id : ids) {
            memberExperienceDeleter.delete(memberId, id);
        }

    }

}
