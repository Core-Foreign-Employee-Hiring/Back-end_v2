package com.forwork.backend.api.order.repository;

import com.forwork.backend.api.order.dto.query.PassArchivePreviewIdAndPaymentApprovedAtQueryDTO;
import com.forwork.backend.api.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o.amount from Order o" +
            " where o.merchantOrderId=:merchantOrderId")
    String findAmountByMerchantOrderId(@Param("merchantOrderId")String merchantOrderId);

    @Query("select o from Order o" +
            " where o.merchantOrderId=:merchantOrderId")
    Optional<Order> findByMerchantOrderId(@Param("merchantOrderId")String merchantOrderId);

    @Query("select o from Order o" +
            " left join fetch o.buyer" +
            " where o.merchantOrderId=:merchantOrderId")
    Optional<Order> findByMerchantOrderIdWithBuyer(@Param("merchantOrderId")String merchantOrderId);


    @Query("select new com.forwork.backend.api.order.dto.query.PassArchivePreviewIdAndPaymentApprovedAtQueryDTO(opa.passArchive.passArchiveId, p.id, p.approvedAt)" +
            " from Order o" +
            " join Payment p on p.order.id=o.id" +
            " join OrderPassArchive opa on opa.order.id=o.id" +
            " where p.paymentStatus='DONE' and o.buyer.id = :memberId" +
            " order by opa.id desc")
    Page<PassArchivePreviewIdAndPaymentApprovedAtQueryDTO> findMyArchiveByMemberId(@Param("memberId")Long memberId, Pageable pageable);

    @Query("select count(*)>0 from Order o" +
            " join OrderPassArchive opa on opa.order.id=o.id" +
            " join Payment p on p.order.id=o.id" +
            " where o.buyer.id=:memberId and opa.passArchive.passArchiveId=:archiveId and p.paymentStatus='DONE'")
    boolean existsPurchasedArchive(@Param("memberId")Long memberId, @Param("archiveId")Long archiveId);
}
