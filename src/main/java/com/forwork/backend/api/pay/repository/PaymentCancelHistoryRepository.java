package com.forwork.backend.api.pay.repository;

import com.forwork.backend.api.pay.entity.PaymentCancelHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCancelHistoryRepository extends JpaRepository<PaymentCancelHistory, Long> {
}
