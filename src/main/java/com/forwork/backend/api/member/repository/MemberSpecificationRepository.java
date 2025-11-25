package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.MemberSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSpecificationRepository extends JpaRepository<MemberSpecification, Long> {
}
