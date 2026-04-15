package com.forwork.backend.api.order.repository;

import com.forwork.backend.api.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem oi" +
            " left join fetch oi.item" +
            " where oi.order.id=:orderId")
    List<OrderItem> findByOrderIdWithItem(@Param("orderId") Long orderId);
}
