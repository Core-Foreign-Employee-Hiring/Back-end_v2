package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.request.LanguageSkillRequestDTO;
import com.forwork.backend.api.member_specification.entity.MemberLanguageSkill;
import com.forwork.backend.api.member_specification.entity.MemberSpecification;
import com.forwork.backend.api.member_specification.repository.MemberLanguageSkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberLanguageSkillCreator {
    private final MemberLanguageSkillRepository memberLanguageSkillRepository;


    @Transactional
    public void create(MemberSpecification memberSpec,
                       LanguageSkillRequestDTO dto) {

        List<LanguageSkillRequestDTO.LanguageSkill> languageSkills =
                Optional.ofNullable(dto.languageSkills()).orElse(List.of());

        for (LanguageSkillRequestDTO.LanguageSkill languageSkill : languageSkills) {
            create(memberSpec, languageSkill);
        }
    }


    @Transactional
    public void create(MemberSpecification memberSpec,
                       LanguageSkillRequestDTO.LanguageSkill languageSkill) {

        MemberLanguageSkill memberLanguageSkill = MemberLanguageSkill.builder()
                .title(languageSkill.title())
                .score(languageSkill.score())
                .memberSpecification(memberSpec)
                .build();

        memberLanguageSkillRepository.save(memberLanguageSkill);
    }


}
