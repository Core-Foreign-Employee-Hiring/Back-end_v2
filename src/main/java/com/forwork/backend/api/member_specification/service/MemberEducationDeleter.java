package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.entity.MemberEducation;
import com.forwork.backend.api.member_specification.repository.MemberEducationRepository;
import com.forwork.backend.api.member_specification.repository.MemberMajorRepository;
import com.forwork.backend.common.exception.ForbiddenException;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.forwork.backend.common.response.ErrorStatus.SPEC_ACCESS_DENIED_EXCEPTION;
import static com.forwork.backend.common.response.ErrorStatus.SPEC_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberEducationDeleter {
    private final MemberEducationRepository memberEducationRepository;
    private final MemberMajorRepository memberMajorRepository;

    @Transactional
    public void delete(Long memberId, Long educationId) {
        // 소유자 검증
        MemberEducation memberEducation = memberEducationRepository.findByIdWithMember(educationId)
                .orElseThrow(() -> {
                    log.warn("[delete][스펙 없음.][educationId= {}]", educationId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        Long ownerId = memberEducation.getMemberSpecification().getMember().getId();

        if (!memberId.equals(ownerId)) {
            log.warn("[delete][본인 거 아닌데 삭제][memberId= {}, ownerId= {}]", memberId, ownerId);
            throw new ForbiddenException(SPEC_ACCESS_DENIED_EXCEPTION.getMessage());
        }

        // 삭제

        memberMajorRepository.deleteByMemberEducationId(educationId);
        memberEducationRepository.delete(memberEducation);
    }
}
