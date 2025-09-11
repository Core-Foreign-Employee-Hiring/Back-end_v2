package com.forwork.backend.api.settlement.repository;

import com.forwork.backend.api.settlement.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByMemberId(Long memberId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Account a
           set a.accountName   = :name,
               a.accountNumber = :number,
               a.bankName      = :bank
         where a.member.id     = :memberId
    """)
    int updateByMemberId(@Param("memberId") Long memberId, @Param("name") String name, @Param("number") String number, @Param("bank") String bank);
}
