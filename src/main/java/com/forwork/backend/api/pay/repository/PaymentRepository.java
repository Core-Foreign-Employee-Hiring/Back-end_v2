package com.forwork.backend.api.pay.repository;

import com.forwork.backend.api.pay.entity.Payment;
import com.forwork.backend.api.pay.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p" +
            " where p.paymentKey=:paymentKey")
    Optional<Payment> findByPaymentKey(@Param("paymentKey") String paymentKey);

    @Query("select p from Payment p" +
            " where p.paymentStatus in :statuses and p.createdDate <= :beforeDate")
    List<Payment> findPaymentsCreatedBefore(@Param("statuses") List<PaymentStatus> statuses, @Param("beforeDate") LocalDate beforeDate, Pageable pageable);

    @Query("select p from Payment p" +
            " where p.paymentStatus = :status and p.createdDate <= :beforeDate")
    List<Payment> findPaymentCreatedBefore(@Param("status") PaymentStatus status, @Param("beforeDate") LocalDate beforeDate, Pageable pageable);

    @Query("select distinct payment.id from Payout payout" +
            " join Payment payment on payment.payout.id=payout.id" +
            " where payout.seller.id=:sellerId")
    List<Long> findPaymentIdsBySellerId(@Param("sellerId") Long sellerId);

    @Query("select distinct p.id from Payment p" +
            " join p.order o" +
            " join OrderPassArchive opa on opa.order.id=o.id" +
            " join PassArchive a on a.passArchiveId=opa.passArchive.passArchiveId" +
            " where a.member.id=:writerId and p.paymentStatus='DONE'")
    Page<Long> findPaymentIdsByWriter(@Param("writerId") Long writerId, Pageable pageable);

    @Query("select p from Payment p" +
            " join fetch p.order o" +
            " where p.id in :paymentIds")
    List<Payment> findByPaymentIdsWithOrder(@Param("paymentIds") List<Long> paymentIds);

    @Query(value = "SELECT COALESCE(SUM(CAST(p.total_amount AS DECIMAL(19,4))), 0) " +
            "FROM payment p " +
            "JOIN orders o ON p.order_id = o.order_id " +
            "JOIN order_pass_archive opa ON opa.order_id = o.order_id " +
            "JOIN pass_archive a ON a.pass_archive_id = opa.pass_archive_id " +
            "WHERE a.member_id = :memberId AND p.payment_status = 'DONE'", nativeQuery = true)
    BigDecimal findTotalSalesRevenue(@Param("memberId") Long memberId);


    @Query("select p from Payment p" +
            " join fetch p.order o" +
            " where o.buyer.id=:buyerId" +
            " order by o.id desc")
    Page<Payment> findByBuyerIdWithOrder(@Param("buyerId") Long buyerId, Pageable pageable);


    @Transactional
    @Modifying
    @Query("update Payment p set p.paymentStatus=:paymentStatus where p.paymentKey=:paymentKey")
    void updatePaymentStatusByPaymentKey(@Param("paymentKey") String paymentKey, @Param("paymentStatus") PaymentStatus PaymentStatus);

}
