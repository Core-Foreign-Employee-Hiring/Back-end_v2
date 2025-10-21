package com.forwork.backend.api.recruit.repository;

import com.forwork.backend.api.recruit.entity.LanguageTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LanguageTypeEntityRepository extends JpaRepository<LanguageTypeEntity, Long> {
    @Query("select j from LanguageTypeEntity j " +
            "where j.languageType in :languageTypes")
    List<LanguageTypeEntity> findAllByLanguageTypes(@Param("languageTypes") List<String> languageTypes);
}
