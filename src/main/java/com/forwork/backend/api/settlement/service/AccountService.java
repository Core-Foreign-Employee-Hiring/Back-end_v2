package com.forwork.backend.api.settlement.service;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.api.member.repository.MemberRepository;
import com.forwork.backend.api.settlement.dto.AccountAddRequestDTO;
import com.forwork.backend.api.settlement.dto.AccountResponseDTO;
import com.forwork.backend.api.settlement.entity.Account;
import com.forwork.backend.api.settlement.repository.AccountRepository;
import com.forwork.backend.common.exception.BadRequestException;
import com.forwork.backend.common.exception.NotFoundException;
import com.forwork.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    // 게좌정보 등록
    @Transactional
    public void addAccount(AccountAddRequestDTO accountAddRequestDTO, Long memberId) {

        // 회원 체크
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        Account account = Account.builder()
                .accountName(accountAddRequestDTO.getAccountName())
                .accountNumber(accountAddRequestDTO.getAccountNumber())
                .bankName(accountAddRequestDTO.getBankName())
                .member(member)
                .build();

        accountRepository.save(account);
    }

    // 계좌정보 조회
    @Transactional(readOnly = true)
    public AccountResponseDTO getAccount(Long memberId) {

        // 회원 체크
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage());
        }

        // 계좌정보 체크
        Account account = accountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.ACCOUT_NOT_FOUND_EXCEPTION.getMessage()));

        return new AccountResponseDTO(
                account.getAccountName(),
                account.getAccountNumber(),
                account.getBankName()
        );
    }

    // 계좌정보 수정
    @Transactional
    public void modifyAccount(AccountAddRequestDTO accountAddRequestDTO, Long memberId) {

        // 회원 체크
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage());
        }

        // 게좌정보 체크
        Account account = accountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.ACCOUT_NOT_FOUND_EXCEPTION.getMessage()));

        account.updateAccount(accountAddRequestDTO.getAccountName(), accountAddRequestDTO.getAccountNumber(), accountAddRequestDTO.getBankName());
    }

}