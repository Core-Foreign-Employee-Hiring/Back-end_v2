package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.request.ExperienceRequestDTO;
import com.forwork.backend.api.member_specification.entity.MemberExperience;
import com.forwork.backend.api.member_specification.entity.MemberSpecification;
import com.forwork.backend.api.member_specification.repository.MemberExperienceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberExperienceCreator {
    private final MemberExperienceRepository memberExperienceRepository;

    @Transactional
    public void create(MemberSpecification memberSpec,
                       ExperienceRequestDTO dto) {

        List<ExperienceRequestDTO.Experience> experiences =
                Optional.ofNullable(dto.experiences()).orElse(List.of());

        for (ExperienceRequestDTO.Experience experience : experiences) {
            create(memberSpec, experience);
        }
    }


    @Transactional
    public void create(MemberSpecification memberSpec,
                       ExperienceRequestDTO.Experience experience) {

        MemberExperience memberExperience = MemberExperience.builder()
                .experience(experience.experience())
                .beforeImprovementRate(experience.beforeImprovementRate())
                .afterImprovementRate(experience.afterImprovementRate())
                .description(experience.description())
                .startDate(experience.startDate())
                .endDate(experience.endDate())
                .memberSpecification(memberSpec)
                .build();

        memberExperienceRepository.save(memberExperience);
    }

}
