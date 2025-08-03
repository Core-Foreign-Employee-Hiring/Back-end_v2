package com.forwork.backend.api.order.repository;

import com.forwork.backend.api.order.entity.OrderPassArchive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPassArchiveRepository extends JpaRepository<OrderPassArchive, Long> {
}
