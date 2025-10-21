package com.forwork.backend.api.recruit.enums;

import lombok.Getter;

@Getter
public enum WorkRegion {
    SEOUL("SEOUL", "서울특별시", "서울"),
    GYEONGGI("GYEONGGI", "경기도", "경기"),
    INCHEON("INCHEON", "인천광역시", "인천"),
    BUSAN("BUSAN", "부산광역시", "부산"),
    DAEJEON("DAEJEON", "대전광역시", "대전"),
    DAEGU("DAEGU", "대구광역시", "대꾸"),
    ULSAN("ULSAN", "울산광역시", "울산"),
    GWANGJU("GWANGJU", "광주광역시", "광주"),
    GANGWON("GANGWON", "강원특별자치도", "강원특별자치도"),
    SEJONG("SEJONG", "세종특별자치도", "세종특별자치도"),
    CHUNGBUK("CHUNGBUK", "충청북도", "충북"),
    CHUNGNAM("CHUNGNAM", "충청남도", "충남"),
    GYEONGBUK("GYEONGBUK", "경상북도", "경북"),
    GYEONGNAM("GYEONGNAM", "경상남도", "경남"),
    JEJU("JEJU", "제주특별자치도", "제주특별자치도"),
    JEONBUK("JEONBUK", "전라북도", "전북특별자치도"),
    JEONNAM("JEONNAM", "전라남도", "전남"),
    UNKNOWN("UNKNOWN", "알 수 없음", "UNKNOWN"),

    ;

    private final String dbValue;
    private final String koreanName;
    private final String prefix;

    WorkRegion(String dbValue, String koreanName, String prefix) {
        this.dbValue = dbValue;
        this.koreanName = koreanName;
        this.prefix = prefix;
    }


    public static WorkRegion fromKoreanName(String name) {
        for (WorkRegion region : WorkRegion.values()) {
            if (region.koreanName.equals(name)) {
                return region;
            }
        }
        return UNKNOWN;
    }

    public static WorkRegion fromPrefix(String address1) {
        if(address1==null||address1.isEmpty()){return UNKNOWN;}

        String strip = address1.strip();
        for (WorkRegion region : WorkRegion.values()) {
            String prefix = region.prefix;

            if (strip.startsWith(prefix)) {
                return region;
            }
        }

        return UNKNOWN;
    }

    public static WorkRegion getWorkRegionByDBValue(String dbValue) {
        for (WorkRegion region : WorkRegion.values()) {
            if (dbValue.equals(region.dbValue)) {
                return region;
            }
        }
        return null;
    }
}
