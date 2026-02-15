package com.forwork.backend.api.member_specification.service;

import com.forwork.backend.api.member_specification.entity.MemberAward;
import com.forwork.backend.api.member_specification.repository.MemberAwardRepository;
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
public class MemberAwardDeleter {
    private final MemberAwardRepository memberAwardRepository;

    @Transactional
    public void delete(Long memberId, Long awardId) {
        // 소유자 검증
        MemberAward memberAward = memberAwardRepository.findByIdWithMember(awardId)
                .orElseThrow(() -> {
                    log.warn("[delete][스펙 없음.][awardId= {}]", awardId);
                    return new NotFoundException(SPEC_NOT_FOUND_EXCEPTION.getMessage());
                });

        Long ownerId = memberAward.getMemberSpecification().getMember().getId();

        if (!memberId.equals(ownerId)) {
            log.warn("[delete][본인 거 아닌데 삭제][memberId= {}, ownerId= {}]", memberId, ownerId);
            throw new ForbiddenException(SPEC_ACCESS_DENIED_EXCEPTION.getMessage());
        }

        // 삭제

        memberAwardRepository.delete(memberAward);
    }
}
