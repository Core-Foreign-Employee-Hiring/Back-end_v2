package com.forwork.backend.api.member.entity;

import lombok.Getter;

@Getter
public enum Visa {
    A1("A1", "A-1", "외교"),
    A2("A2", "A-2", "공무"),
    A3("A3", "A-3", "협정"),
    B1("B1", "B-1", "사증면제"),
    B2("B2", "B-2", "관광통과"),
    C1("C1", "C-1", "일시취재"),
    C3("C3", "C-3", "단기방문"),
    C4("C4", "C-4", "문화예술"),
    D2("D2", "D-2", "유학"),
    D3("D3", "D-3", "기술연수"),
    D4("D4", "D-4", "일반연수"),
    D5("D5", "D-5", "취재"),
    D6("D6", "D-6", "종교"),
    D7("D7", "D-7", "주재"),
    D8("D8", "D-8", "기업투자"),
    D9("D9", "D-9", "무역경영"),
    D10("D10", "D-10", "구직"),
    E1("E1", "E-1", "교수"),
    E2("E2", "E-2", "회화지도"),
    E3("E3", "E-3", "연구"),
    E4("E4", "E-4", "기술지도"),
    E5("E5", "E-5", "전문직업"),
    E6("E6", "E-6", "예술흥행"),
    E7("E7", "E-7", "특정활동"),
    E8("E8", "E-8", "계절근로"),
    E9("E9", "E-9", "비전문취업"),
    E10("E10", "E-10", "선원취업"),
    F1("F1", "F-1", "방문동거"),
    F2("F2", "F-2", "거주"),
    F3("F3", "F-3", "동반"),
    F4("F4", "F-4", "재외동포"),
    F5("F5", "F-5", "영주"),
    F6("F6", "F-6", "결혼이민"),
    G1("G1", "G-1", "기타"),
    H1("H1", "H-1", "관광취업"),
    H2("H2", "H-2", "방문취업"),
    F27("F27", "F-2-7", "점수제 우수인재");

    private final String dbValue;
    private final String code;
    private final String description;

    Visa(String dbValue, String code, String description) {
        this.dbValue = dbValue;
        this.code = code;
        this.description = description;
    }

    public static Visa getVisaByDBValue(String dbValue){
        for (Visa visa : Visa.values()) {
            if (visa.getDbValue().equals(dbValue)){
                return visa;
            }
        }

        return null;
    }

}
