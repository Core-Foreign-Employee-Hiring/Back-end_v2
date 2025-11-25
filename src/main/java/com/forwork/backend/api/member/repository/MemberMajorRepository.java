package com.forwork.backend.api.member.repository;

import com.forwork.backend.api.member.entity.MemberMajor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberMajorRepository extends JpaRepository<MemberMajor, Long> {
}
