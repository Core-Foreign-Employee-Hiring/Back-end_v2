package com.forwork.backend.api.member.entity;

import com.forwork.backend.api.recruit.entity.RecruitJobRole;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public enum JobRole {
    SOFTWARE_ENGINEER("SOFTWARE_ENGINEER", "소프트웨어 엔지니어"),
    WEB_DEVELOPER("WEB_DEVELOPER", "웹 개발자"),
    BACKEND_DEVELOPER("BACKEND_DEVELOPER", "백엔드 개발자"),
    FRONTEND_DEVELOPER("FRONTEND_DEVELOPER", "프론트엔드 개발자"),
    JAVA_DEVELOPER("JAVA_DEVELOPER", "자바 개발자"),
    C_CPP_DEVELOPER("C_CPP_DEVELOPER", "C/C++ 개발자"),
    PYTHON_DEVELOPER("PYTHON_DEVELOPER", "파이썬 개발자"),
    MACHINE_LEARNING_ENGINEER("MACHINE_LEARNING_ENGINEER", "머신러닝 엔지니어"),
    DEVOPS_ENGINEER("DEVOPS_ENGINEER", "DevOps / 시스템 관리자"),
    DATA_ENGINEER("DATA_ENGINEER", "데이터 엔지니어"),
    NODEJS_DEVELOPER("NODEJS_DEVELOPER", "Node.js 개발자"),
    SYSTEM_NETWORK_ADMIN("SYSTEM_NETWORK_ADMIN", "시스템/네트워크 관리자"),
    ANDROID_DEVELOPER("ANDROID_DEVELOPER", "안드로이드 개발자"),
    IOS_DEVELOPER("IOS_DEVELOPER", "iOS 개발자"),
    EMBEDDED_DEVELOPER("EMBEDDED_DEVELOPER", "임베디드 개발자"),
    TECH_SUPPORT("TECH_SUPPORT", "기술지원"),
    QA_TEST_ENGINEER("QA_TEST_ENGINEER", "QA / 테스트 엔지니어"),
    DATA_SCIENTIST("DATA_SCIENTIST", "데이터 사이언티스트"),
    SECURITY_ENGINEER("SECURITY_ENGINEER", "보안 엔지니어"),
    BIGDATA_ENGINEER("BIGDATA_ENGINEER", "빅데이터 엔지니어"),
    HARDWARE_ENGINEER("HARDWARE_ENGINEER", "하드웨어 엔지니어"),
    BLOCKCHAIN_ENGINEER("BLOCKCHAIN_ENGINEER", "블록체인 엔지니어"),
    CROSS_PLATFORM_APP_DEVELOPER("CROSS_PLATFORM_APP_DEVELOPER", "크로스플랫폼 앱 개발자"),
    DBA("DBA", "DBA"),
    PHP_DEVELOPER("PHP_DEVELOPER", "PHP 개발자"),
    DOTNET_DEVELOPER("DOTNET_DEVELOPER", ".NET 개발자"),
    GRAPHICS_ENGINEER("GRAPHICS_ENGINEER", "그래픽스 엔지니어"),
    AR_VR_ENGINEER("AR_VR_ENGINEER", "AR/VR 엔지니어"),
    RUBY_ON_RAILS_DEVELOPER("RUBY_ON_RAILS_DEVELOPER", "루비온레일즈 개발자"),
    AI("AI", "AI"),

    PM_PO("PM_PO", "PM/PO"),
    PROJECT_MANAGER("PROJECT_MANAGER", "프로젝트 매니저"),
    STRATEGY_PLANNER("STRATEGY_PLANNER", "전략 기획자"),
    OPERATIONS_MANAGER("OPERATIONS_MANAGER", "운영 매니저"),
    DATA_ANALYST("DATA_ANALYST", "데이터 분석가"),
    BRAND_MANAGER("BRAND_MANAGER", "브랜드 매니저"),
    GLOBAL_BUSINESS_DEVELOPER("GLOBAL_BUSINESS_DEVELOPER", "해외 사업개발 / 기획자"),
    CONSULTANT("CONSULTANT", "컨설턴트"),
    PURCHASING_MANAGER("PURCHASING_MANAGER", "구매담당"),
    BUSINESS_INNOVATOR("BUSINESS_INNOVATOR", "경영 혁신가"),
    AGILE_COACH("AGILE_COACH", "애자일코치"),

    MARKETING_MANAGER("MARKETING_MANAGER", "마케팅 매니저"),
    DIGITAL_MARKETER("DIGITAL_MARKETER", "디지털 마케터"),
    CONTENT_MARKETER("CONTENT_MARKETER", "콘텐츠 마케터"),
    PERFORMANCE_MARKETER("PERFORMANCE_MARKETER", "퍼포먼스 마케터"),
    BRAND_MARKETER("BRAND_MARKETER", "브랜드 마케터"),
    GLOBAL_MARKETING_MANAGER("GLOBAL_MARKETING_MANAGER", "글로벌 마케팅 매니저"),
    SNS_MARKETER("SNS_MARKETER", "SNS 마케터"),
    PR_SPECIALIST("PR_SPECIALIST", "PR 전문가"),
    GROWTH_HACKER("GROWTH_HACKER", "그로스 해커"),
    MARKETING_DIRECTOR("MARKETING_DIRECTOR", "마케팅 디렉터"),
    MARKET_RESEARCHER("MARKET_RESEARCHER", "마켓 리서치"),

    UI_UX_DESIGNER("UI_UX_DESIGNER", "UI/UX 디자이너"),
    WEB_DESIGNER("WEB_DESIGNER", "웹 디자이너"),
    GRAPHIC_DESIGNER("GRAPHIC_DESIGNER", "그래픽 디자이너"),
    SPACE_DESIGNER("SPACE_DESIGNER", "공간 디자이너"),
    MOTION_DESIGNER("MOTION_DESIGNER", "영상/모션 디자이너"),
    FASHION_DESIGNER("FASHION_DESIGNER", "패션 디자이너"),
    ART_DIRECTOR("ART_DIRECTOR", "아트 디렉터"),
    INDUSTRIAL_DESIGNER("INDUSTRIAL_DESIGNER", "산업 디자이너"),
    FURNITURE_DESIGNER("FURNITURE_DESIGNER", "가구 디자이너"),
    LANDSCAPE_DESIGNER("LANDSCAPE_DESIGNER", "조경 디자이너"),

    INTERNATIONAL_SALES("INTERNATIONAL_SALES", "해외영업"),
    TECHNICAL_SALES("TECHNICAL_SALES", "기술영업"),
    SOLUTION_CONSULTANT("SOLUTION_CONSULTANT", "솔루션 컨설턴트"),
    MEDIA_SALES("MEDIA_SALES", "미디어 세일즈"),
    CUSTOMER_SUCCESS_MANAGER("CUSTOMER_SUCCESS_MANAGER", "고객성공매니저"),
    SALES_ENGINEER("SALES_ENGINEER", "세일즈 엔지니어"),
    CORPORATE_SALES("CORPORATE_SALES", "법인영업"),
    SALES_MANAGEMENT("SALES_MANAGEMENT", "영업 관리"),


    GLOBAL_CS_MANAGER("GLOBAL_CS_MANAGER", "글로벌 CS 매니저"),
    RETAIL_MD("RETAIL_MD", "리테일 MD"),
    CUSTOMER_SUPPORT("CUSTOMER_SUPPORT", "고객 지원/상담"),
    FASHION_MD("FASHION_MD", "패션 MD"),
    CRM_SPECIALIST("CRM_SPECIALIST", "CRM 전문가"),
    RECEPTIONIST("RECEPTIONIST", "리셉션"),
    TRAVEL_AGENT("TRAVEL_AGENT", "여행 에이전트"),
    FLIGHT_ATTENDANT("FLIGHT_ATTENDANT", "승무원"),
    STORE_CLERK("STORE_CLERK", "매장점원"),
    TOURISM_WORKER("TOURISM_WORKER", "관광숙박업 종사자"),

    INTERPRETER("INTERPRETER", "통역사"),
    TRANSLATOR("TRANSLATOR", "번역가"),
    LOCALIZATION_SPECIALIST("LOCALIZATION_SPECIALIST", "로컬라이제이션 전문가"),

    CONTENT_CREATOR("CONTENT_CREATOR", "콘텐츠 크리에이터"),
    VIDEO_EDITOR("VIDEO_EDITOR", "영상 편집가"),
    VIDEO_PRODUCER("VIDEO_PRODUCER", "비디오 제작"),
    WRITER("WRITER", "작가"),
    PHOTOGRAPHER("PHOTOGRAPHER", "사진작가"),
    JOURNALIST("JOURNALIST", "저널리스트"),
    CURATOR("CURATOR", "큐레이터"),

    ELECTRICAL_ENGINEER("ELECTRICAL_ENGINEER", "전자 엔지니어"),
    ROBOTICS_AUTOMATION_ENGINEER("ROBOTICS_AUTOMATION_ENGINEER", "로봇/자동화"),
    MECHANICAL_ENGINEER("MECHANICAL_ENGINEER", "기계 엔지니어"),
    CAD_3D_DESIGNER("CAD_3D_DESIGNER", "CAD/3D 설계자"),
    ELECTRIC_ENGINEER("ELECTRIC_ENGINEER", "전기 엔지니어"),
    CONTROL_ENGINEER("CONTROL_ENGINEER", "제어 엔지니어"),
    PRODUCT_ENGINEER("PRODUCT_ENGINEER", "제품 엔지니어"),
    ELECTROMECHANICAL_ENGINEER("ELECTROMECHANICAL_ENGINEER", "전자기계 공학자"),
    EQUIPMENT_ENGINEER("EQUIPMENT_ENGINEER", "장비 엔지니어"),
    QA_ENGINEER("QA_ENGINEER", "QA 엔지니어"),
    INDUSTRIAL_ENGINEER("INDUSTRIAL_ENGINEER", "산업 엔지니어"),
    RF_ENGINEER("RF_ENGINEER", "RF 엔지니어"),
    CHEMICAL_ENGINEER("CHEMICAL_ENGINEER", "화학공학 엔지니어"),
    AEROSPACE_ENGINEER("AEROSPACE_ENGINEER", "항공우주 엔지니어"),
    IC_ENGINEER("IC_ENGINEER", "I&C 엔지니어"),
    MATERIAL_ENGINEER("MATERIAL_ENGINEER", "재료공학자"),
    PLANT_ENGINEER("PLANT_ENGINEER", "플랜트 엔지니어"),
    PLASTIC_ENGINEER("PLASTIC_ENGINEER", "플라스틱 엔지니어"),
    QC_ENGINEER("QC_ENGINEER", "QC 엔지니어"),
    STRUCTURAL_ENGINEER("STRUCTURAL_ENGINEER", "구조공학 엔지니어"),
    CONSTRUCTION_ENGINEER("CONSTRUCTION_ENGINEER", "건설 엔지니어"),
    CIVIL_ENGINEER("CIVIL_ENGINEER", "토목 엔지니어"),
    ENVIRONMENTAL_ENGINEER("ENVIRONMENTAL_ENGINEER", "환경 엔지니어"),
    PRODUCTION_ENGINEER("PRODUCTION_ENGINEER", "생산공학 엔지니어"),
    RND_RESEARCHER("RND_RESEARCHER", "R&D / 연구원"),

    GLOBAL_HR_MANAGER("GLOBAL_HR_MANAGER", "글로벌 HR 매니저"),
    RECRUITER("RECRUITER", "리크루터"),
    HR_CONSULTANT("HR_CONSULTANT", "HR 컨설턴트"),
    TECH_TRAINER("TECH_TRAINER", "기술 교육"),
    INHOUSE_TRAINER("INHOUSE_TRAINER", "사내 강사"),

    GAME_PLANNER("GAME_PLANNER", "게임 기획자"),
    GAME_ARTIST("GAME_ARTIST", "게임 아티스트"),
    GAME_CLIENT_DEVELOPER("GAME_CLIENT_DEVELOPER", "게임 클라이언트 개발자"),
    UNITY_DEVELOPER("UNITY_DEVELOPER", "유니티 개발자"),
    GAME_GRAPHIC_DESIGNER("GAME_GRAPHIC_DESIGNER", "게임 그래픽 디자이너"),
    GAME_SERVER_DEVELOPER("GAME_SERVER_DEVELOPER", "게임 서버 개발자"),
    MOBILE_GAME_DEVELOPER("MOBILE_GAME_DEVELOPER", "모바일 게임 개발자"),
    UNREAL_DEVELOPER("UNREAL_DEVELOPER", "언리얼 개발자"),

    INVESTMENT_BANKER("INVESTMENT_BANKER", "투자은행가"),
    ASSET_MANAGER("ASSET_MANAGER", "자산 운용가"),
    FINANCIAL_ENGINEER("FINANCIAL_ENGINEER", "금융공학자"),

    MACHINE_TECHNICIAN("MACHINE_TECHNICIAN", "기계제작 기술자"),
    MANUFACTURING_TEST_ENGINEER("MANUFACTURING_TEST_ENGINEER", "제조 테스트 엔지니어"),
    MANUFACTURING_ENGINEER("MANUFACTURING_ENGINEER", "제조 엔지니어"),
    MANUFACTURING_CHEMIST("MANUFACTURING_CHEMIST", "제조 화학자"),
    SEMICONDUCTOR_DISPLAY_ENGINEER("SEMICONDUCTOR_DISPLAY_ENGINEER", "반도체/디스플레이 엔지니어"),
    PRODUCTION_WORKER("PRODUCTION_WORKER", "생산직 종사자"),

    INSTRUCTOR("INSTRUCTOR", "강사"),
    LANGUAGE_EDUCATOR("LANGUAGE_EDUCATOR", "외국어교육"),

    BIOTECH_RESEARCHER("BIOTECH_RESEARCHER", "생명공학 연구원"),
    CLINICAL_RESEARCHER("CLINICAL_RESEARCHER", "임상시험 연구원"),
    MICROBIOLOGIST("MICROBIOLOGIST", "미생물학자"),
    HOSPITAL_COORDINATOR("HOSPITAL_COORDINATOR", "병원 코디네이터"),
    PHARMACEUTICAL_CHEMIST("PHARMACEUTICAL_CHEMIST", "약학 분석 화학자"),
    GENETIC_ENGINEER("GENETIC_ENGINEER", "유전공학자"),
    CAREGIVER("CAREGIVER", "요양보호사"),

    LOGISTICS_MANAGER("LOGISTICS_MANAGER", "물류담당"),
    LOGISTICS_ANALYST("LOGISTICS_ANALYST", "물류 분석가"),
    EXPORT_IMPORT_OFFICER("EXPORT_IMPORT_OFFICER", "수출입사무"),
    TRADE_OFFICER("TRADE_OFFICER", "무역사무"),
    BUYER_MANAGER("BUYER_MANAGER", "바이어관리/상담/개발"),
    AIR_TRANSPORT_AGENT("AIR_TRANSPORT_AGENT", "항공 운송"),
    MARINE_TRANSPORT_AGENT("MARINE_TRANSPORT_AGENT", "해운/해양 운송"),
    LOGISTICS_FIELD_WORKER("LOGISTICS_FIELD_WORKER", "물류 현장직 종사자"),

    FOOD_SERVICE_WORKER("FOOD_SERVICE_WORKER", "외식업 종사자"),
    CHEF("CHEF", "요리사"),
    MENU_DEVELOPER("MENU_DEVELOPER", "메뉴개발"),
    BARTENDER("BARTENDER", "바텐더"),
    SOMMELIER("SOMMELIER", "소믈리에"),
    FOOD_STYLIST("FOOD_STYLIST", "푸드스타일리스트"),

    ARCHITECT("ARCHITECT", "건축가"),
    CONSTRUCTION_SUPERVISOR("CONSTRUCTION_SUPERVISOR", "건축 감리자"),
    MAINTENANCE_MANAGER("MAINTENANCE_MANAGER", "유지보수 관리자"),
    CONSTRUCTION_WORKER("CONSTRUCTION_WORKER", "건설 현장직 종사자"),
    WELDER("WELDER", "용접공"),
    CARPENTER("CARPENTER", "목수"),
    HEAVY_EQUIPMENT_TECHNICIAN("HEAVY_EQUIPMENT_TECHNICIAN", "중장비 기술자"),

    MODEL("MODEL", "모델"),
    ACTOR("ACTOR", "배우"),
    SHOW_HOST("SHOW_HOST", "방송 진행자"),
    
    ;


    private final String dbValue;
    private final String koreanName;

    JobRole(String dbValue, String koreanName) {
        this.dbValue = dbValue;
        this.koreanName = koreanName;
    }

    public static JobRole getJobRole(String dbValue) {
        for (JobRole jobRole : JobRole.values()) {
            if (jobRole.dbValue.equals(dbValue)) {
                return jobRole;
            }
        }
        return null;
    }

    public static List<JobRole> convertToJobRolesByRecruit(Set<RecruitJobRole> recruitJobRoles){
        List<JobRole> jobRoles = new ArrayList<>();
        for (RecruitJobRole recruitJobRole : recruitJobRoles) {
            String jobRole = recruitJobRole.getJobRoleEntity().getJobRole();
            JobRole jobRole1 = JobRole.getJobRole(jobRole);
            jobRoles.add(jobRole1);
        }
        return jobRoles;
    }


    public static List<JobRole> convertToJobRolesByMember(Set<MemberJobRole> memberJobRoles){
        List<JobRole> jobRoles = new ArrayList<>();
        for (MemberJobRole memberJobRole : memberJobRoles) {
            String jobRole = memberJobRole.getJobRoleEntity().getJobRole();
            JobRole jobRole1 = JobRole.getJobRole(jobRole);
            jobRoles.add(jobRole1);
        }
        return jobRoles;
    }

}
