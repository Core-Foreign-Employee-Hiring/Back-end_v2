package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.internal.MemberSpecificationDTO;
import com.forwork.backend.api.member_specification.entity.*;
import com.forwork.backend.api.member_specification.repository.*;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.forwork.backend.common.response.ErrorStatus.SPEC_NOT_FOUND_EXCEPTION;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberSpecificationReader {
    private final MemberSpecificationRepository memberSpecificationRepository;
    private final MemberEducationRepository memberEducationRepository;
    private final MemberMajorRepository memberMajorRepository;
    private final MemberLanguageSkillRepository memberLanguageSkillRepository;
    private final MemberCertificationRepository memberCertificationRepository;
    private final MemberCareerRepository memberCareerRepository;
    private final MemberAwardRepository memberAwardRepository;
    private final MemberExperienceRepository memberExperienceRepository;



    public MemberSpecificationDTO getMemberSpecification(Long memberId){
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

        MemberSpecificationDTO.Education education =
                memberEducationRepository.findByMemberSpecificationId(memberSpecificationId)
                        .map(memberEducation -> {
                            // 전공 조회
                            List<String> majors = memberMajorRepository.findAllByMemberEducationId(memberEducation.getId());
                            return MemberSpecificationDTO.Education.of(memberEducation, majors);
                        })
                        .orElse(null);

        /*
         * 어학
         * */

        List<MemberSpecificationDTO.LanguageSkill> languageSkills = memberLanguageSkillRepository.findByMemberSpecificationId(memberSpecificationId).stream()
                .map(MemberSpecificationDTO.LanguageSkill::of)
                .toList();


        /*
         * 자격증
         * */

        List<MemberCertification> memberCertifications = memberCertificationRepository.findByMemberSpecification(memberSpecificationId);

        List<MemberSpecificationDTO.Certification> certifications = memberCertifications.stream()
                .map(MemberSpecificationDTO.Certification::of)
                .toList();

        /*
         * 경력
         * */

        List<MemberCareer> memberCareers = memberCareerRepository.findByMemberSpecificationId(memberSpecificationId);

        List<MemberSpecificationDTO.Career> careers = memberCareers.stream()
                .map(MemberSpecificationDTO.Career::of)
                .toList();


        /*
         * 수상
         * */
        List<MemberAward> memberAwards = memberAwardRepository.findByMemberSpecificationId(memberSpecificationId);

        List<MemberSpecificationDTO.Award> awards = memberAwards.stream()
                .map(MemberSpecificationDTO.Award::of)
                .toList();

        /*
         * 경험
         * */

        List<MemberExperience> memberExperiences = memberExperienceRepository.findByMemberSpecificationId(memberSpecificationId);

        List<MemberSpecificationDTO.Experience> experiences = memberExperiences.stream()
                .map(MemberSpecificationDTO.Experience::of)
                .toList();

        MemberSpecificationDTO response = MemberSpecificationDTO.of(
                memberSpecificationId,
                education,
                languageSkills,
                certifications,
                careers,
                awards,
                experiences
        );

        return response;
    }
}
