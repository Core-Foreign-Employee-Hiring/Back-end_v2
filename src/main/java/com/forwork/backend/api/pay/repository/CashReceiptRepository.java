package com.forwork.backend.api.pay.repository;

import com.forwork.backend.api.pay.entity.CashReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CashReceiptRepository extends JpaRepository<CashReceipt, Long> {
    @Query("select cr from CashReceipt cr " +
            "where cr.merchantOrderId = :merchantOrderId")
    Optional<CashReceipt> findByMerchantOrderId(@Param("merchantOrderId") String merchantOrderId);

}
