package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member.entity.*;
import com.forwork.backend.api.member.repository.*;
import com.forwork.backend.api.member_specification.dto.internal.MemberSpecEvaluationInternalDTO;
import com.forwork.backend.api.member_specification.dto.internal.MemberSpecificationDTO;
import com.forwork.backend.api.member_specification.dto.request.MemberSpecificationRequestDTO;
import com.forwork.backend.api.member_specification.dto.response.MemberSpecEvaluationResponseDTO;
import com.forwork.backend.api.member_specification.dto.response.MemberSpecificationResponseDTO;
import com.forwork.backend.api.member_specification.entity.*;
import com.forwork.backend.api.member_specification.repository.*;
import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.common.exception.InternalServerException;
import com.forwork.backend.common.exception.NotFoundException;
import com.forwork.backend.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.forwork.backend.common.response.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberSpecificationService {
    private final MemberSpecificationRepository memberSpecificationRepository;
    private final MemberEducationRepository memberEducationRepository;
    private final MemberRepository memberRepository;
    private final MemberMajorRepository memberMajorRepository;
    private final MemberLanguageSkillRepository memberLanguageSkillRepository;
    private final MemberCertificationRepository memberCertificationRepository;
    private final MemberCareerRepository memberCareerRepository;
    private final MemberAwardRepository memberAwardRepository;
    private final MemberEnglishSkillRepository memberEnglishSkillRepository;
    private final MemberExperienceRepository memberExperienceRepository;
    private final MemberSpecificationReader memberSpecificationReader;
    private final MemberSpecEvaluationClient memberSpecEvaluationClient;
    private final SpecificationEvaluationRepository specificationEvaluationRepository;


    /*
     * c
     * */

    /**
     * 스펙 입력
     */
    @Transactional
    public void createMemberSpecification(Long memberId, MemberSpecificationRequestDTO memberSpecificationRequestDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[createMemberSpecification][member is not found][memberId= {}]", memberId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        /*
         * 스펙 생성
         * */

        MemberSpecification memberSpec = MemberSpecification.builder()
                .member(member)
                .build();

        memberSpecificationRepository.save(memberSpec);


        /*
         * 학력
         * */

        MemberSpecificationRequestDTO.Education education = memberSpecificationRequestDTO.education();


        MemberEducation memberEducation = MemberEducation.builder()
                .schoolName(education.schoolName())
                .earnedScore(education.earnedScore())
                .maxScore(education.maxScore())
                .memberSpecification(memberSpec)
                .build();

        memberEducationRepository.save(memberEducation);

        // 전공
        List<MemberMajor> memberMajors = education.majors().stream()
                .map(major -> MemberMajor.builder()
                        .major(major)
                        .memberEducation(memberEducation)
                        .build())
                .toList();

        memberMajorRepository.saveAll(memberMajors);


        /*
         * 어학
         * */

        MemberSpecificationRequestDTO.LanguageSkill languageSkill = memberSpecificationRequestDTO.languageSkill();

        MemberLanguageSkill memberLanguageSkill = MemberLanguageSkill.builder()
                .klptScore(languageSkill.klptScore())
                .memberSpecification(memberSpec)
                .build();


        memberLanguageSkillRepository.save(memberLanguageSkill);

        // 영어
        Optional.ofNullable(languageSkill.englishSkills())
                .ifPresent(englishSkills -> {
                    List<MemberEnglishSkill> memberEnglishSkills = englishSkills.stream()
                            .map(englishSkill -> MemberEnglishSkill.builder()  // 엔티티 변환
                                    .type(englishSkill.type())
                                    .score(englishSkill.score())
                                    .memberLanguageSkill(memberLanguageSkill)
                                    .build()
                            )
                            .toList();

                    memberEnglishSkillRepository.saveAll(memberEnglishSkills);
                });


        /*
         * 자격증
         * */

        Optional.ofNullable(memberSpecificationRequestDTO.certifications())
                .ifPresent(certifications -> {
                    List<MemberCertification> memberCertifications = certifications.stream()
                            .map(certification -> MemberCertification.builder()   // 엔티티 변환
                                    .certificationName(certification.certificationName())
                                    .acquiredYear(certification.acquiredYear())
                                    .acquiredMonth(certification.acquiredMonth())
                                    .documentUrl(certification.documentUrl())
                                    .memberSpecification(memberSpec)
                                    .build()
                            )
                            .toList();

                    memberCertificationRepository.saveAll(memberCertifications);
                });


        /*
         * 경력
         * */

        Optional.ofNullable(memberSpecificationRequestDTO.careers())
                .ifPresent(careers -> {
                    List<MemberCareer> memberCareers = careers.stream()
                            .map(career -> MemberCareer.builder()
                                    .companyName(career.companyName())
                                    .position(career.position())
                                    .startYear(career.startYear())
                                    .startMonth(career.startMonth())
                                    .endYear(career.endYear())
                                    .endMonth(career.endMonth())
                                    .contractType(Optional.ofNullable(career.contractType()).map(ContractType::name).orElse(null))
                                    .highlight(career.highlight())
                                    .memberSpecification(memberSpec)
                                    .build()
                            ).toList();

                    memberCareerRepository.saveAll(memberCareers);
                });

        /*
         * 수상
         * */
        Optional.ofNullable(memberSpecificationRequestDTO.awards())
                .ifPresent(awards -> {
                    List<MemberAward> memberAwards = awards.stream()
                            .map(award -> MemberAward.builder()
                                    .awardName(award.awardName())
                                    .host(award.host())
                                    .acquiredYear(award.acquiredYear())
                                    .acquiredMonth(award.acquiredMonth())
                                    .description(award.description())
                                    .documentUrl(award.documentUrl())
                                    .memberSpecification(memberSpec)
                                    .build()
                            ).toList();

                    memberAwardRepository.saveAll(memberAwards);
                });

        /*
         * 경험
         * */

        Optional.ofNullable(memberSpecificationRequestDTO.experiences())
                .ifPresent(experiences -> {
                    List<MemberExperience> memberExperiences = experiences.stream()
                            .map(experience ->
                                    MemberExperience.builder()
                                            .experience(experience.experience())
                                            .beforeImprovementRate(experience.beforeImprovementRate())
                                            .afterImprovementRate(experience.afterImprovementRate())
                                            .description(experience.description())
                                            .insight(experience.insight())
                                            .memberSpecification(memberSpec)
                                            .build()
                            )
                            .toList();

                    memberExperienceRepository.saveAll(memberExperiences);

                });
    }

    /**
     * 스펙 평가
     */

    public Long evaluateSpecification(Long memberId){
        MemberSpecificationDTO memberSpecificationDTO = memberSpecificationReader.getMemberSpecification(memberId);

        // ai 서버에거 갖고 온다.
        MemberSpecEvaluationInternalDTO memberSpecEvaluationInternalDTO = memberSpecEvaluationClient.evaluateSpecification(memberSpecificationDTO);

        if(!memberSpecEvaluationInternalDTO.success()){
            HttpStatus httpStatus = memberSpecEvaluationInternalDTO.httpStatus();

            throw new InternalServerException("서버 내부 오류가 발생했습니다.");
        }


        // score 계산 (일단, 오각형 총합)
        int score=memberSpecEvaluationInternalDTO.experience()+memberSpecEvaluationInternalDTO.certificate()
                +memberSpecEvaluationInternalDTO.language()+memberSpecEvaluationInternalDTO.career()
                +memberSpecEvaluationInternalDTO.education();

        // DB 저장.

        MemberSpecification memberSpecification = memberSpecificationRepository.findById(memberSpecificationDTO.memberSpecificationId())
                .orElseThrow(() -> {
                    log.warn("[evaluateSpecification][스펙 없음.][memberSpecificationId= {}]", memberSpecificationDTO.memberSpecificationId());
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        SpecificationEvaluation specificationEvaluation = SpecificationEvaluation.builder()
                .experience(memberSpecEvaluationInternalDTO.experience())
                .certificate(memberSpecEvaluationInternalDTO.certificate())
                .language(memberSpecEvaluationInternalDTO.language())
                .career(memberSpecEvaluationInternalDTO.career())
                .education(memberSpecEvaluationInternalDTO.education())
                .score(score)
                .analysis(memberSpecEvaluationInternalDTO.analysis())
                .memberSpecification(memberSpecification)
                .build();

        Long id = specificationEvaluationRepository.save(specificationEvaluation).getId();

        return id;

    }

    /*
    * r
    * */

    /**
     * 입력한 스펙 조회
     */

    public MemberSpecificationResponseDTO getMemberSpecification(Long memberId){
        MemberSpecificationDTO memberSpecification = memberSpecificationReader.getMemberSpecification(memberId);

        MemberSpecificationResponseDTO response = MemberSpecificationResponseDTO.of(memberSpecification);
        return response;
    }

    /**
     * 스펙 평가 조회
     */
    public MemberSpecEvaluationResponseDTO getSpecEvaluation(Long memberId, Long specEvaluationId){

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

        if(!memberSpecification.getId().equals(specificationEvaluation.getMemberSpecification().getId())){
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

}
