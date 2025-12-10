package com.forwork.backend.api.member.service;

import com.forwork.backend.api.member.dto.MemberSpecificationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class MemberSpecTextConverter {

    public static String toText(MemberSpecificationDTO dto) {

        StringBuilder sb = new StringBuilder();

        // Education
        if (dto.education() != null) {
            sb.append("학력: ")
                    .append(dto.education().schoolName());
            if (dto.education().majors() != null && !dto.education().majors().isEmpty()) {
                sb.append(", 전공: ").append(String.join(", ", dto.education().majors()));
            }
            if (dto.education().earnedScore() != null) {
                sb.append(String.format(", 학점 %.2f/%.2f",
                        dto.education().earnedScore(),
                        dto.education().maxScore()));
            }
            sb.append(". ");
        }

        // Language
        if (dto.languageSkill() != null) {
            sb.append("어학: ");
            if (dto.languageSkill().klptScore() != null) {
                sb.append("KLPT ").append(dto.languageSkill().klptScore()).append("점");
            }

            if (dto.languageSkill().englishSkills() != null && !dto.languageSkill().englishSkills().isEmpty()) {
                String englishStr = dto.languageSkill().englishSkills().stream()
                        .map(es -> es.type() + " " + es.score())
                        .collect(Collectors.joining(", "));

                sb.append(", 영어: ").append(englishStr);
            }
            sb.append(". ");
        }

        // Certifications
        if (dto.certifications() != null && !dto.certifications().isEmpty()) {
            String certText = dto.certifications().stream()
                    .map(MemberSpecificationDTO.Certification::certificationName)
                    .collect(Collectors.joining(", "));
            sb.append("자격증: ").append(certText).append(". ");
        }

        // Career
        if (dto.careers() != null && !dto.careers().isEmpty()) {
            dto.careers().forEach(c -> {
                sb.append("경력: ").append(c.companyName()).append("에서 ")
                        .append(c.position())
                        .append(String.format(" (%d.%d ~ %d.%d)",
                                c.startYear(), c.startMonth(),
                                c.endYear() != null ? c.endYear() : 0,
                                c.endMonth() != null ? c.endMonth() : 0))
                        .append(". ");
            });
        }

        // Awards
        if (dto.awards() != null && !dto.awards().isEmpty()) {
            String awardsText = dto.awards().stream()
                    .map(a -> a.awardName() + " 수상")
                    .collect(Collectors.joining(", "));
            sb.append("수상: ").append(awardsText).append(". ");
        }

        // Experiences
        if (dto.experiences() != null && !dto.experiences().isEmpty()) {
            dto.experiences().forEach(ex -> {
                sb.append("경험: ").append(ex.experience());
                if (ex.beforeImprovementRate() != null && ex.afterImprovementRate() != null) {
                    sb.append(String.format(", 개선율 %.1f%% → %.1f%%",
                            ex.beforeImprovementRate(), ex.afterImprovementRate()));
                }
                if (ex.description() != null)
                    sb.append(", ").append(ex.description());
                sb.append(". ");
            });
        }

        return sb.toString().trim();
    }
}
