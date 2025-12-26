package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.internal.MemberSpecificationDTO;
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
                sb.append(", 전공: ")
                        .append(String.join(", ", dto.education().majors()));
            }

            if (dto.education().admissionDate() != null) {
                sb.append(", 재학기간: ")
                        .append(dto.education().admissionDate())
                        .append(" ~ ");

                if (dto.education().graduationDate() != null) {
                    sb.append(dto.education().graduationDate());
                } else {
                    sb.append("재학 중");
                }
            }

            if (dto.education().earnedScore() != null && dto.education().maxScore() != null) {
                sb.append(String.format(", 학점 %.2f/%.2f",
                        dto.education().earnedScore(),
                        dto.education().maxScore()));
            }

            sb.append(". ");
        }


        // Language
        if (dto.languageSkills() != null && !dto.languageSkills().isEmpty()) {
            sb.append("어학: ");

            String languageStr = dto.languageSkills().stream()
                    .map(ls -> ls.title() + " " + ls.score())
                    .collect(Collectors.joining(", "));

            sb.append(languageStr).append(". ");
        }


        // Certifications
        if (dto.certifications() != null && !dto.certifications().isEmpty()) {
            String certText = dto.certifications().stream()
                    .map(c -> {
                        if (c.acquiredDate() != null) {
                            return c.certificationName() + " (" + c.acquiredDate() + ")";
                        }
                        return c.certificationName();
                    })
                    .collect(Collectors.joining(", "));

            sb.append("자격증: ").append(certText).append(". ");
        }


        // Career
        if (dto.careers() != null && !dto.careers().isEmpty()) {
            dto.careers().forEach(c -> {
                sb.append("경력: ")
                        .append(c.companyName())
                        .append("에서 ")
                        .append(c.position());

                if (c.startDate() != null) {
                    sb.append(" (")
                            .append(c.startDate())
                            .append(" ~ ");

                    if (c.endDate() != null) {
                        sb.append(c.endDate());
                    } else {
                        sb.append("재직 중");
                    }

                    sb.append(")");
                }

                sb.append(". ");
            });
        }


        // Awards
        if (dto.awards() != null && !dto.awards().isEmpty()) {
            String awardsText = dto.awards().stream()
                    .map(a -> {
                        StringBuilder awardSb = new StringBuilder();

                        // 수상명
                        awardSb.append(a.awardName()).append(" 수상");

                        // 주최
                        if (a.host() != null) {
                            awardSb.append(" (").append(a.host()).append(")");
                        }

                        // 취득일
                        if (a.acquiredDate() != null) {
                            awardSb.append(" - ").append(a.acquiredDate());
                        }

                        // 설명 (null 아니면 추가)
                        if (a.description() != null) {
                            awardSb.append(": ").append(a.description());
                        }

                        return awardSb.toString();
                    })
                    .collect(Collectors.joining(", "));

            sb.append("수상: ").append(awardsText).append(". ");
        }


        // Experiences
        if (dto.experiences() != null && !dto.experiences().isEmpty()) {
            dto.experiences().forEach(ex -> {
                sb.append("경험: ")
                        .append(ex.experience());

                // 기간
                if (ex.startDate() != null) {
                    sb.append(" (")
                            .append(ex.startDate())
                            .append(" ~ ");

                    if (ex.endDate() != null) {
                        sb.append(ex.endDate());
                    } else {
                        sb.append("진행 중");
                    }
                    sb.append(")");
                }

                // 개선율 (선택)
                if (ex.beforeImprovementRate() != null && ex.afterImprovementRate() != null) {
                    sb.append(String.format(
                            ", 개선율 %.1f%% → %.1f%%",
                            ex.beforeImprovementRate(),
                            ex.afterImprovementRate()
                    ));
                }

                // 설명
                if (ex.description() != null) {
                    sb.append(", ").append(ex.description());
                }

                sb.append(". ");
            });
        }


        return sb.toString().trim();
    }
}
