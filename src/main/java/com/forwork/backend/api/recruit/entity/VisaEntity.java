package com.forwork.backend.api.recruit.entity;

import com.forwork.backend.api.member.entity.Visa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class VisaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="visa_entity_id")
    private Long id;

    private String visa;

    public VisaEntity(Visa visa) {
        this.visa = visa.getDbValue();
    }
}
