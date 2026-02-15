package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.request.CertificationRequestDTO;
import com.forwork.backend.api.member_specification.entity.MemberCertification;
import com.forwork.backend.api.member_specification.entity.MemberSpecification;
import com.forwork.backend.api.member_specification.repository.MemberCertificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberCertificationCreator {
    private final MemberCertificationRepository memberCertificationRepository;

    @Transactional
    public void create(MemberSpecification memberSpec,
                       CertificationRequestDTO dto) {

        List<CertificationRequestDTO.Certification> certifications =
                Optional.ofNullable(dto.certifications()).orElse(List.of());

        for (CertificationRequestDTO.Certification certification : certifications) {
            create(memberSpec, certification);
        }
    }


    @Transactional
    public void create(MemberSpecification memberSpec,
                       CertificationRequestDTO.Certification certification) {

        MemberCertification memberCertification = MemberCertification.builder()
                .certificationName(certification.certificationName())
                .acquiredDate(certification.acquiredDate())
                .documentUrl(certification.documentUrl())
                .memberSpecification(memberSpec)
                .build();

        memberCertificationRepository.save(memberCertification);
    }

}
