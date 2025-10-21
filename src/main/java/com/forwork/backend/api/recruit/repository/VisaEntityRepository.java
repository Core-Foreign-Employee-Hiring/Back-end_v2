package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.recruit.entity.VisaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VisaEntityRepository extends JpaRepository<VisaEntity, Long> {
    @Query("select j from VisaEntity j " +
            "where j.visa in :visas")
    List<VisaEntity> findAllByVisas(@Param("visas") List<String> visas);
}
