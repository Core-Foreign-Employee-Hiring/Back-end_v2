package com.forwork.backend.api.order.repository;

import com.forwork.backend.api.order.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i" +
            " where i.id in :itemIds and i.active=true")
    List<Item> findActiveItemsByIds(@Param("itemIds") List<Long> itemIds);

}
