package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.entity.MemberExperience;
import com.forwork.backend.api.member_specification.repository.MemberExperienceRepository;
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
public class MemberExperienceDeleter {
    private final MemberExperienceRepository memberExperienceRepository;

    @Transactional
    public void delete(Long memberId, Long experienceId) {
        // 소유자 검증
        MemberExperience memberExperience = memberExperienceRepository.findByIdWithMember(experienceId)
                .orElseThrow(() -> {
                    log.warn("[delete][스펙 없음.][experienceId= {}]", experienceId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        Long ownerId = memberExperience.getMemberSpecification().getMember().getId();

        if (!memberId.equals(ownerId)) {
            log.warn("[delete][본인 거 아닌데 삭제][memberId= {}, ownerId= {}]", memberId, ownerId);
            throw new ForbiddenException(SPEC_ACCESS_DENIED_EXCEPTION.getMessage());
        }

        // 삭제

        memberExperienceRepository.delete(memberExperience);
    }
}
