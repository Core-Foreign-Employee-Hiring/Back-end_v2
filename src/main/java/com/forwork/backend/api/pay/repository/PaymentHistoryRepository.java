package com.forwork.backend.api.pay.repository;

import com.forwork.backend.api.pay.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}
