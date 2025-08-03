package com.forwork.backend.api.pay.repository;

import com.forwork.backend.api.pay.entity.PaymentReconcileHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentReconcileHistoryRepository extends JpaRepository<PaymentReconcileHistory, Long> {
}
