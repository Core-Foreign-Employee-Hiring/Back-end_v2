package com.forwork.backend.api.member.service;

import com.forwork.backend.api.member.dto.MemberSpecificationRequestDTO;
import com.forwork.backend.api.member.dto.MemberSpecificationResponseDTO;
import com.forwork.backend.api.member.entity.*;
import com.forwork.backend.api.member.repository.*;
import com.forwork.backend.api.recruit.enums.ContractType;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final MemberEnglishSkillRepository englishSkillRepository;
    private final MemberCertificationRepository memberCertificationRepository;
    private final MemberCareerRepository memberCareerRepository;
    private final MemberAwardRepository memberAwardRepository;
    private final MemberExperienceRepository experienceRepository;
    private final MemberEnglishSkillRepository memberEnglishSkillRepository;
    private final MemberExperienceRepository memberExperienceRepository;


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

                    englishSkillRepository.saveAll(memberEnglishSkills);
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

                    experienceRepository.saveAll(memberExperiences);

                });
    }

    /*
    * r
    * */

    /**
     * 입력한 스펙 조회
     */

    public MemberSpecificationResponseDTO getMemberSpecification(Long memberId){
        /*
         * 스펙
         * */

        MemberSpecification memberSpecification = memberSpecificationRepository.findByMemberIdId(memberId)
                .orElseThrow(() -> {
                    log.warn("[getMemberSpecification][스펙 없음.][memberId= {}]", memberId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });
        Long memberSpecificationId = memberSpecification.getId();

        /*
         * 학력
         * */

        MemberSpecificationResponseDTO.Education education =
                memberEducationRepository.findByMemberSpecificationId(memberSpecificationId)
                        .map(memberEducation -> {
                            // 전공 조회
                            List<String> majors = memberMajorRepository.findAllByMemberEducationId(memberEducation.getId());
                            return MemberSpecificationResponseDTO.Education.of(memberEducation, majors);
                        })
                        .orElse(null);


        /*
         * 어학
         * */

        MemberSpecificationResponseDTO.LanguageSkill languageSkill = memberLanguageSkillRepository.findByMemberSpecificationId(memberSpecificationId)
                .map(memberLanguageSkill -> {
                    // 영어 시험 조회
                    List<MemberEnglishSkill> byMemberLanguageSkillId = memberEnglishSkillRepository.findByMemberLanguageSkillId(memberLanguageSkill.getId());
                    return MemberSpecificationResponseDTO.LanguageSkill.of(memberLanguageSkill, byMemberLanguageSkillId);
                })
                .orElse(null);



        /*
         * 자격증
         * */

        List<MemberCertification> memberCertifications = memberCertificationRepository.findByMemberSpecification(memberSpecificationId);

        List<MemberSpecificationResponseDTO.Certification> certifications = memberCertifications.stream()
                .map(MemberSpecificationResponseDTO.Certification::of)
                .toList();

        /*
         * 경력
         * */

        List<MemberCareer> memberCareers = memberCareerRepository.findByMemberSpecificationId(memberSpecificationId);

        List<MemberSpecificationResponseDTO.Career> careers = memberCareers.stream()
                .map(MemberSpecificationResponseDTO.Career::of)
                .toList();


        /*
         * 수상
         * */
        List<MemberAward> memberAwards = memberAwardRepository.findByMemberSpecificationId(memberSpecificationId);

        List<MemberSpecificationResponseDTO.Award> awards = memberAwards.stream()
                .map(MemberSpecificationResponseDTO.Award::of)
                .toList();

        /*
         * 경험
         * */

        List<MemberExperience> memberExperiences = memberExperienceRepository.findByMemberSpecificationId(memberSpecificationId);

        List<MemberSpecificationResponseDTO.Experience> experiences = memberExperiences.stream()
                .map(MemberSpecificationResponseDTO.Experience::of)
                .toList();

        MemberSpecificationResponseDTO response = MemberSpecificationResponseDTO.of(
                education,
                languageSkill,
                certifications,
                careers,
                awards,
                experiences
        );

        return response;
    }

}
