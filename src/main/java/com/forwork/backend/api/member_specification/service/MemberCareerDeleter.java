package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.entity.MemberCareer;
import com.forwork.backend.api.member_specification.repository.MemberCareerRepository;
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
public class MemberCareerDeleter {
    private final MemberCareerRepository memberCareerRepository;

    @Transactional
    public void delete(Long memberId, Long careerId) {
        // 소유자 검증
        MemberCareer memberCareer = memberCareerRepository.findByIdWithMember(careerId)
                .orElseThrow(() -> {
                    log.warn("[delete][스펙 없음.][careerId= {}]", careerId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        Long ownerId = memberCareer.getMemberSpecification().getMember().getId();

        if (!memberId.equals(ownerId)) {
            log.warn("[delete][본인 거 아닌데 삭제][memberId= {}, ownerId= {}]", memberId, ownerId);
            throw new ForbiddenException(SPEC_ACCESS_DENIED_EXCEPTION.getMessage());
        }

        // 삭제

        memberCareerRepository.delete(memberCareer);
    }
}
