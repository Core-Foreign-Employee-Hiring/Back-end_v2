package com.forwork.backend.api.member.entity;

import lombok.Getter;

@Getter
public enum JobCategory {
    DESIGN("디자인"),
    PRODUCTION_MANUFACTURING("생산/제조"),
    IT("IT"),
    MANAGEMENT_OFFICE("경영/사무"),
    MARKETING_ADVERTISING("마케팅/광고"),
    EDUCATION("교육"),
    TRADE_LOGISTICS("무역/물류"),
    SALES_CS("영업/CS"),
    SERVICE("서비스"),
    CONSTRUCTION("건설"),
    ENTERTAINMENT("엔터테인먼트"),
    TRANSLATION("번역"),
    R_AND_D("R&D"),
    ETC("기타");

    private final String description;

    JobCategory(String description) {
        this.description = description;
    }
}