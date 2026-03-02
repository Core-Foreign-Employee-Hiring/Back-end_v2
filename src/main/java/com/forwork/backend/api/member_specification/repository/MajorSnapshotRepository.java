package com.forwork.backend.api.member_specification.repository;

import com.forwork.backend.api.member_specification.entity.MajorSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MajorSnapshotRepository extends JpaRepository<MajorSnapshot, Long> {
    @Query("select m.major from MajorSnapshot m" +
            " where m.educationSnapshot.id=:educationId")
    List<String> findAllByEducationId(@Param("educationId") Long educationId);
}
