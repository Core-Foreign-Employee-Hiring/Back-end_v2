package com.forwork.backend.api.item.repository;

import com.forwork.backend.api.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i" +
            " where i.id in :itemIds and i.active=true")
    List<Item> findActiveItemsByIds(@Param("itemIds") List<Long> itemIds);

    @Query("select i from OrderItem oi" +
            " join oi.item i" +
            " where oi.order.merchantOrderId=:merchantOrderId")
    List<Item> findItemsByMerchantOrderId(@Param("merchantOrderId") String merchantOrderId);

    @Query("select i from Item i " +
            "where i.active = true and i.type = :type" +
            " order by i.id desc")
    Page<Item> findActiveItemsByType(@Param("type") String type, Pageable pageable);
}
