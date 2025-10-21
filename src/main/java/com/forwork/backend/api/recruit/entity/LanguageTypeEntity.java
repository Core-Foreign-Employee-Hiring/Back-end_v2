package com.forwork.backend.api.recruit.entity;

import com.forwork.backend.api.recruit.enums.LanguageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class LanguageTypeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="language_type_entity_id")
    private Long id;

    private String languageType;

    public LanguageTypeEntity(LanguageType languageType) {
        this.languageType = languageType.getDbValue();
    }
}
