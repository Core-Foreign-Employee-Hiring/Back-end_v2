package com.forwork.backend.api.pay.repository;

import com.forwork.backend.api.pay.entity.CashReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashReceiptRepository extends JpaRepository<CashReceipt, Long> {

}
