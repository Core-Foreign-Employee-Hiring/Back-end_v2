package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.request.CareerRequestDTO;
import com.forwork.backend.api.member_specification.entity.MemberCareer;
import com.forwork.backend.api.member_specification.entity.MemberSpecification;
import com.forwork.backend.api.member_specification.repository.MemberCareerRepository;
import com.forwork.backend.api.recruit.enums.ContractType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberCareerCreator {
    private final MemberCareerRepository memberCareerRepository;


    @Transactional
    public void create(MemberSpecification memberSpec,
                       CareerRequestDTO dto) {

        List<CareerRequestDTO.Career> careers =
                Optional.ofNullable(dto.careers()).orElse(List.of());

        for (CareerRequestDTO.Career career : careers) {
            create(memberSpec, career);
        }
    }


    @Transactional
    public void create(MemberSpecification memberSpec,
                       CareerRequestDTO.Career career) {

        MemberCareer memberCareer = MemberCareer.builder()
                .companyName(career.companyName())
                .position(career.position())
                .startDate(career.startDate())
                .endDate(career.endDate())
                .contractType(Optional.ofNullable(career.contractType()).map(ContractType::name).orElse(null))
                .highlight(career.highlight())
                .memberSpecification(memberSpec)
                .build();

        memberCareerRepository.save(memberCareer);
    }

}
