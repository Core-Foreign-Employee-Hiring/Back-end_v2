package com.forwork.backend.api.pay.repository;

import com.forwork.backend.api.pay.entity.Payout;
import com.forwork.backend.api.pay.enums.PayoutStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PayoutRepositoryQueryDSL {
    Page<Payout> findBySellerIdAndPayoutStatus(Long sellerId, PayoutStatus payoutStatus, Pageable pageable);
}
