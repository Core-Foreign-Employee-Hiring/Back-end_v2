package com.forwork.backend.api.recruit.enums;

import lombok.Getter;

@Getter
public enum LanguageType {
    ENGLISH("ENGLISH", "영어", "English"),
    CHINESE("CHINESE", "중국어", "Chinese / Mandarin"),
    HINDI("HINDI", "힌디어", "Hindi"),
    SPANISH("SPANISH", "스페인어", "Spanish"),
    FRENCH("FRENCH", "프랑스어", "French"),
    ARABIC("ARABIC", "아랍어", "Arabic"),
    BENGALI("BENGALI", "벵골어", "Bengali"),
    PORTUGUESE("PORTUGUESE", "포르투갈어", "Portuguese"),
    RUSSIAN("RUSSIAN", "러시아어", "Russian"),
    URDU("URDU", "우르두어", "Urdu"),
    INDONESIAN("INDONESIAN", "인도네시아어", "Indonesian"),
    GERMAN("GERMAN", "독일어", "German"),
    JAPANESE("JAPANESE", "일본어", "Japanese"),
    SWAHILI("SWAHILI", "스와힐리어", "Swahili"),
    MARATHI("MARATHI", "마라티어", "Marathi"),
    TELUGU("TELUGU", "텔루구어", "Telugu"),
    TURKISH("TURKISH", "튀르키예어", "Turkish"),
    TAMIL("TAMIL", "타밀어", "Tamil"),
    VIETNAMESE("VIETNAMESE", "베트남어", "Vietnamese"),
    KOREAN("KOREAN", "한국어", "Korean"),
    ITALIAN("ITALIAN", "이탈리아어", "Italian"),
    PERSIAN("PERSIAN", "페르시아어", "Persian"),
    POLISH("POLISH", "폴란드어", "Polish"),
    UKRAINIAN("UKRAINIAN", "우크라이나어", "Ukrainian"),
    THAI("THAI", "태국어", "Thai")

    ;


    private final String dbValue;
    private final String koreanName;
    private final String englishName;

    LanguageType(String dbValue, String koreanName, String englishName) {
        this.dbValue = dbValue;
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

    public static LanguageType fromKoreanName(String name) {
        for (LanguageType lang : values()) {
            if (lang.koreanName.equals(name)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Unknown language (Korean): " + name);
    }

    public static LanguageType fromEnglishName(String name) {
        for (LanguageType lang : values()) {
            if (lang.englishName.equalsIgnoreCase(name)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Unknown language (English): " + name);
    }

    public static LanguageType getLanguageByDBValue(String dbValue) {
        for (LanguageType lang : values()) {
            if (lang.dbValue.equals(dbValue)) {
                return lang;
            }
        }
        return null;
    }
}
