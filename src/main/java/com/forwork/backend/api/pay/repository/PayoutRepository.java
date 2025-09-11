package com.forwork.backend.api.pay.repository;

import com.forwork.backend.api.pay.entity.Payout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayoutRepository extends JpaRepository<Payout, Long>, PayoutRepositoryQueryDSL{
}