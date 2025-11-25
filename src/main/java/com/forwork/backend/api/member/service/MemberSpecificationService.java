package com.forwork.backend.api.member.service;

import com.forwork.backend.api.member.dto.MemberSpecificationRequestDTO;
import com.forwork.backend.api.member.entity.*;
import com.forwork.backend.api.member.repository.*;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.forwork.backend.common.response.ErrorStatus.USER_NOT_FOUND_EXCEPTION;

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
                                    .build()
                            )
                            .toList();

                    memberCertificationRepository.saveAll(memberCertifications);
                });

    }
}
