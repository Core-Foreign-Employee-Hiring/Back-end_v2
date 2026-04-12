package com.forwork.backend.api.member.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.forwork.backend.common.response.ErrorStatus.USER_NOT_FOUND_EXCEPTION;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberReader {
    private final MemberRepository memberRepository;

    public Member getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[getMember][멤버 없음.][memberId= {}]", memberId);
                    return new NotFoundException(USER_NOT_FOUND_EXCEPTION.getMessage());
                });

        return member;
    }

}
