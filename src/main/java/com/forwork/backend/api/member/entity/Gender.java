package com.forwork.backend.api.member.entity;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    NULL("무관");

    private final String description;

    Gender(String description) {
        this.description = description;
    }
}