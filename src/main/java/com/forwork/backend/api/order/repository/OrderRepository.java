package com.forwork.backend.api.order.repository;

import com.forwork.backend.api.order.entity.Order;
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
    Optional<Order> findByOrderId(@Param("merchantOrderId")String merchantOrderId);
}
