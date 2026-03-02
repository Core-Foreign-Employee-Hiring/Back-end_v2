package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.external.response.MemberSpecEvaluationExternalResponseDTO;
import com.forwork.backend.api.member_specification.dto.internal.MemberSpecificationDTO;
import com.forwork.backend.api.member_specification.entity.*;
import com.forwork.backend.api.member_specification.repository.*;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.forwork.backend.common.response.ErrorStatus.SPEC_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSpecificationService {
    private final MemberSpecificationRepository memberSpecificationRepository;
    private final SpecificationEvaluationRepository specificationEvaluationRepository;
    private final AwardSnapshotRepository awardSnapshotRepository;
    private final CareerSnapshotRepository careerSnapshotRepository;
    private final CertificationSnapshotRepository certificationSnapshotRepository;
    private final EducationSnapshotRepository educationSnapshotRepository;
    private final ExperienceSnapshotRepository experienceSnapshotRepository;
    private final LanguageSkillSnapshotRepository languageSkillSnapshotRepository;
    private final MajorSnapshotRepository majorSnapshotRepository;


    /*
     * create
     * */

    @Transactional
    public Long createEvaluationWithSnapshots(MemberSpecificationDTO memberSpecificationDTO, String specName, MemberSpecEvaluationExternalResponseDTO memberSpecEvaluationExternalResponseDTO) {

        // score 계산 (일단, 오각형 총합)
        int score = memberSpecEvaluationExternalResponseDTO.experience() + memberSpecEvaluationExternalResponseDTO.certificate()
                + memberSpecEvaluationExternalResponseDTO.language() + memberSpecEvaluationExternalResponseDTO.career()
                + memberSpecEvaluationExternalResponseDTO.education();

        MemberSpecification memberSpecification = memberSpecificationRepository.findById(memberSpecificationDTO.memberSpecificationId())
                .orElseThrow(() -> {
                    log.warn("[createEvaluationWithSnapshots][스펙 없음.][memberSpecificationId= {}]", memberSpecificationDTO.memberSpecificationId());
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });


        // 스펙 평가 저장
        SpecificationEvaluation evaluation = SpecificationEvaluation.builder()
                .experience(memberSpecEvaluationExternalResponseDTO.experience())
                .certificate(memberSpecEvaluationExternalResponseDTO.certificate())
                .language(memberSpecEvaluationExternalResponseDTO.language())
                .career(memberSpecEvaluationExternalResponseDTO.career())
                .education(memberSpecEvaluationExternalResponseDTO.education())
                .score(score)
                .analysis(memberSpecEvaluationExternalResponseDTO.analysis())
                .specName(specName)
                .evaluatedDate(LocalDate.now(ZoneId.of("Asia/Seoul")))
                .memberSpecification(memberSpecification)
                .build();

        Long id = specificationEvaluationRepository.save(evaluation).getId();

        // 스냅샷 저장
        saveEducationSnapshot(memberSpecificationDTO, evaluation);
        saveLanguageSkillSnapshots(memberSpecificationDTO, evaluation);
        saveCertificationSnapshots(memberSpecificationDTO, evaluation);
        saveCareerSnapshots(memberSpecificationDTO, evaluation);
        saveAwardSnapshots(memberSpecificationDTO, evaluation);
        saveExperienceSnapshots(memberSpecificationDTO, evaluation);

        return id;

    }

    private void saveEducationSnapshot(MemberSpecificationDTO dto, SpecificationEvaluation evaluation) {

        if (dto.education() == null) {
            return;
        }

        MemberSpecificationDTO.Education edu = dto.education();

        EducationSnapshot snapshot = EducationSnapshot.builder()
                .schoolName(edu.schoolName())
                .admissionDate(edu.admissionDate())
                .graduationDate(edu.graduationDate())
                .earnedScore(edu.earnedScore())
                .maxScore(edu.maxScore())
                .specificationEvaluation(evaluation)
                .build();

        educationSnapshotRepository.save(snapshot);

        // Major Snapshot
        if (edu.majors() != null) {
            List<MajorSnapshot> majors = edu.majors().stream()
                    .map(major -> MajorSnapshot.builder()
                            .major(major)
                            .educationSnapshot(snapshot)
                            .build())
                    .toList();

            majorSnapshotRepository.saveAll(majors);
        }
    }

    private void saveLanguageSkillSnapshots(MemberSpecificationDTO dto, SpecificationEvaluation evaluation) {

        List<LanguageSkillSnapshot> snapshots = dto.languageSkills().stream()
                .map(skill -> LanguageSkillSnapshot.builder()
                        .title(skill.title())
                        .score(skill.score())
                        .specificationEvaluation(evaluation)
                        .build())
                .toList();

        languageSkillSnapshotRepository.saveAll(snapshots);
    }

    private void saveCertificationSnapshots(MemberSpecificationDTO dto, SpecificationEvaluation evaluation) {

        List<CertificationSnapshot> snapshots = dto.certifications().stream()
                .map(cert -> CertificationSnapshot.builder()
                        .certificationName(cert.certificationName())
                        .acquiredDate(cert.acquiredDate())
                        .documentUrl(cert.documentUrl())
                        .specificationEvaluation(evaluation)
                        .build())
                .toList();

        certificationSnapshotRepository.saveAll(snapshots);
    }

    private void saveCareerSnapshots(MemberSpecificationDTO dto, SpecificationEvaluation evaluation) {

        List<CareerSnapshot> snapshots = dto.careers().stream()
                .map(career -> CareerSnapshot.builder()
                        .companyName(career.companyName())
                        .position(career.position())
                        .startDate(career.startDate())
                        .endDate(career.endDate())
                        .contractType(career.contractType().name())
                        .highlight(career.highlight())
                        .specificationEvaluation(evaluation)
                        .build())
                .toList();

        careerSnapshotRepository.saveAll(snapshots);
    }

    private void saveAwardSnapshots(MemberSpecificationDTO dto, SpecificationEvaluation evaluation) {

        List<AwardSnapshot> snapshots = dto.awards().stream()
                .map(award -> AwardSnapshot.builder()
                        .awardName(award.awardName())
                        .host(award.host())
                        .acquiredDate(award.acquiredDate())
                        .description(award.description())
                        .documentUrl(award.documentUrl())
                        .specificationEvaluation(evaluation)
                        .build())
                .toList();

        awardSnapshotRepository.saveAll(snapshots);
    }

    private void saveExperienceSnapshots(MemberSpecificationDTO dto, SpecificationEvaluation evaluation) {

        List<ExperienceSnapshot> snapshots = dto.experiences().stream()
                .map(exp -> ExperienceSnapshot.builder()
                        .experience(exp.experience())
                        .beforeImprovementRate(exp.beforeImprovementRate())
                        .afterImprovementRate(exp.afterImprovementRate())
                        .description(exp.description())
                        .startDate(exp.startDate())
                        .endDate(exp.endDate())
                        .specificationEvaluation(evaluation)
                        .build())
                .toList();

        experienceSnapshotRepository.saveAll(snapshots);
    }

}
