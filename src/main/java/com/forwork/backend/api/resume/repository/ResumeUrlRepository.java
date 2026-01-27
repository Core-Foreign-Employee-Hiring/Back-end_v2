package com.forwork.backend.api.resume.repository;

import com.forwork.backend.api.resume.entity.ResumeUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeUrlRepository extends JpaRepository<ResumeUrl, Long> {
    List<ResumeUrl> findByResumeId(Long resumeId);
    void deleteByResumeId(Long resumeId);
}
