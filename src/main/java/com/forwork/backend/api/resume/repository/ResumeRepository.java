package com.forwork.backend.api.resume.repository;

import com.forwork.backend.api.resume.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByMemberId(Long memberId);
    Page<Resume> findByMemberId(Long memberId, Pageable pageable);
}
