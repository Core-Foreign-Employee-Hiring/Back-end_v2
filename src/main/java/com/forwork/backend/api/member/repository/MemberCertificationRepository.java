package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.MemberCertification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCertificationRepository extends JpaRepository<MemberCertification, Long> {
}
