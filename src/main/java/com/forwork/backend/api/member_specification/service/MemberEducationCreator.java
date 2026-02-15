package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.dto.request.EducationRequestDTO;
import com.forwork.backend.api.member_specification.entity.MemberEducation;
import com.forwork.backend.api.member_specification.entity.MemberMajor;
import com.forwork.backend.api.member_specification.entity.MemberSpecification;
import com.forwork.backend.api.member_specification.repository.MemberEducationRepository;
import com.forwork.backend.api.member_specification.repository.MemberMajorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberEducationCreator {
    private final MemberEducationRepository memberEducationRepository;
    private final MemberMajorRepository memberMajorRepository;


    /**
     * 학력 생성
     */
    @Transactional
    public void create(MemberSpecification memberSpec, EducationRequestDTO educationRequestDTO) {
        /*
         * 학력
         * */

        MemberEducation memberEducation = MemberEducation.builder()
                .schoolName(educationRequestDTO.schoolName())
                .admissionDate(educationRequestDTO.admissionDate())
                .graduationDate(educationRequestDTO.graduationDate())
                .earnedScore(educationRequestDTO.earnedScore())
                .maxScore(educationRequestDTO.maxScore())
                .memberSpecification(memberSpec)
                .build();

        memberEducationRepository.save(memberEducation);

        // 전공
        List<MemberMajor> memberMajors = educationRequestDTO.majors().stream()
                .map(major -> MemberMajor.builder()
                        .major(major)
                        .memberEducation(memberEducation)
                        .build())
                .toList();

        memberMajorRepository.saveAll(memberMajors);

    }
}
