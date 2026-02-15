package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.request.AwardCreateDTO;
import com.forwork.backend.api.member_specification.entity.MemberAward;
import com.forwork.backend.api.member_specification.entity.MemberSpecification;
import com.forwork.backend.api.member_specification.repository.MemberAwardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberAwardCreator {
    private final MemberAwardRepository memberAwardRepository;


    @Transactional
    public void create(MemberSpecification memberSpec, AwardCreateDTO dto) {

        List<AwardCreateDTO.Award> awards =
                Optional.ofNullable(dto.awards()).orElse(List.of());

        for (AwardCreateDTO.Award award : awards) {
            create(memberSpec, award);
        }
    }


    @Transactional
    public void create(MemberSpecification memberSpec, AwardCreateDTO.Award award) {

        MemberAward memberAward = MemberAward.builder()
                .awardName(award.awardName())
                .host(award.host())
                .acquiredDate(award.acquiredDate())
                .description(award.description())
                .documentUrl(award.documentUrl())
                .memberSpecification(memberSpec)
                .build();

        memberAwardRepository.save(memberAward);
    }

}
