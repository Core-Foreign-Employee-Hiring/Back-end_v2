package com.forwork.backend.api.member.entity;

import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;


import static com.forwork.backend.api.member.entity.JobRole.*;

@Getter
public enum JobCategory {
    IT_DEVELOPMENT("IT_DEVELOPMENT", "IT/개발", EnumSet.of(
            SOFTWARE_ENGINEER,
            WEB_DEVELOPER,
            BACKEND_DEVELOPER,
            FRONTEND_DEVELOPER,
            JAVA_DEVELOPER,
            C_CPP_DEVELOPER,
            PYTHON_DEVELOPER,
            MACHINE_LEARNING_ENGINEER,
            DEVOPS_ENGINEER,
            DATA_ENGINEER,
            NODEJS_DEVELOPER,
            SYSTEM_NETWORK_ADMIN,
            ANDROID_DEVELOPER,
            IOS_DEVELOPER,
            EMBEDDED_DEVELOPER,
            TECH_SUPPORT,
            QA_TEST_ENGINEER,
            DATA_SCIENTIST,
            SECURITY_ENGINEER,
            BIGDATA_ENGINEER,
            HARDWARE_ENGINEER,
            BLOCKCHAIN_ENGINEER,
            CROSS_PLATFORM_APP_DEVELOPER,
            DBA,
            PHP_DEVELOPER,
            DOTNET_DEVELOPER,
            GRAPHICS_ENGINEER,
            AR_VR_ENGINEER,
            RUBY_ON_RAILS_DEVELOPER,
            AI

    )),

    BUSINESS_MANAGEMENT("BUSINESS_MANAGEMENT", "경영/비즈니스", EnumSet.of(
            PM_PO,
            PROJECT_MANAGER,
            STRATEGY_PLANNER,
            OPERATIONS_MANAGER,
            DATA_ANALYST,
            BRAND_MANAGER,
            GLOBAL_BUSINESS_DEVELOPER,
            CONSULTANT,
            PURCHASING_MANAGER,
            BUSINESS_INNOVATOR,
            AGILE_COACH

    )),

    MARKETING_ADVERTISING("MARKETING_ADVERTISING", "마케팅/광고", EnumSet.of(
            MARKETING_MANAGER,
            DIGITAL_MARKETER,
            CONTENT_MARKETER,
            PERFORMANCE_MARKETER,
            BRAND_MARKETER,
            GLOBAL_MARKETING_MANAGER,
            SNS_MARKETER,
            PR_SPECIALIST,
            GROWTH_HACKER,
            MARKETING_DIRECTOR,
            MARKET_RESEARCHER

    )),

    DESIGN("DESIGN", "디자인", EnumSet.of(
            UI_UX_DESIGNER,
            WEB_DESIGNER,
            GRAPHIC_DESIGNER,
            SPACE_DESIGNER,
            MOTION_DESIGNER,
            FASHION_DESIGNER,
            ART_DIRECTOR,
            INDUSTRIAL_DESIGNER,
            FURNITURE_DESIGNER,
            LANDSCAPE_DESIGNER

    )),

    SALES("SALES", "영업", EnumSet.of(
            INTERNATIONAL_SALES,
            TECHNICAL_SALES,
            SOLUTION_CONSULTANT,
            MEDIA_SALES,
            CUSTOMER_SUCCESS_MANAGER,
            SALES_ENGINEER,
            CORPORATE_SALES,
            SALES_MANAGEMENT
    )),

    CUSTOMER_SERVICE_RETAIL("CUSTOMER_SERVICE_RETAIL", "고객서비스/리테일", EnumSet.of(
            GLOBAL_CS_MANAGER,
            RETAIL_MD,
            CUSTOMER_SUPPORT,
            FASHION_MD,
            CRM_SPECIALIST,
            RECEPTIONIST,
            TRAVEL_AGENT,
            FLIGHT_ATTENDANT,
            STORE_CLERK,
            TOURISM_WORKER

    )),

    TRANSLATION_INTERPRETATION("TRANSLATION_INTERPRETATION", "통/번역", EnumSet.of(
            INTERPRETER,
            TRANSLATOR,
            LOCALIZATION_SPECIALIST
    )),



    MEDIA("MEDIA", "미디어", EnumSet.of(
            CONTENT_CREATOR,
            VIDEO_EDITOR,
            VIDEO_PRODUCER,
            WRITER,
            PHOTOGRAPHER,
            JOURNALIST,
            CURATOR

    )),

    ENGINEERING_DESIGN("ENGINEERING_DESIGN", "엔지니어링/설계", EnumSet.of(
            ELECTRICAL_ENGINEER,
            ROBOTICS_AUTOMATION_ENGINEER,
            MECHANICAL_ENGINEER,
            CAD_3D_DESIGNER,
            ELECTRIC_ENGINEER,
            CONTROL_ENGINEER,
            PRODUCT_ENGINEER,
            ELECTROMECHANICAL_ENGINEER,
            EQUIPMENT_ENGINEER,
            QA_ENGINEER,
            INDUSTRIAL_ENGINEER,
            RF_ENGINEER,
            CHEMICAL_ENGINEER,
            AEROSPACE_ENGINEER,
            IC_ENGINEER,
            MATERIAL_ENGINEER,
            PLANT_ENGINEER,
            PLASTIC_ENGINEER,
            QC_ENGINEER,
            STRUCTURAL_ENGINEER,
            CONSTRUCTION_ENGINEER,
            CIVIL_ENGINEER,
            ENVIRONMENTAL_ENGINEER,
            PRODUCTION_ENGINEER,
            RND_RESEARCHER

    )),

    HR("HR", "HR", EnumSet.of(
            GLOBAL_HR_MANAGER,
            RECRUITER,
            HR_CONSULTANT,
            TECH_TRAINER,
            INHOUSE_TRAINER

    )),

    GAME_PRODUCTION("GAME_PRODUCTION", "게임 제작", EnumSet.of(
            GAME_PLANNER,
            GAME_ARTIST,
            GAME_CLIENT_DEVELOPER,
            UNITY_DEVELOPER,
            GAME_GRAPHIC_DESIGNER,
            GAME_SERVER_DEVELOPER,
            MOBILE_GAME_DEVELOPER,
            UNREAL_DEVELOPER

    )),

    FINANCE("FINANCE", "금융", EnumSet.of(
            INVESTMENT_BANKER,
            ASSET_MANAGER,
            FINANCIAL_ENGINEER

    )),

    MANUFACTURING_PRODUCTION("MANUFACTURING_PRODUCTION", "제조/생산", EnumSet.of(
            MACHINE_TECHNICIAN,
            MANUFACTURING_TEST_ENGINEER,
            MANUFACTURING_ENGINEER,
            MANUFACTURING_CHEMIST,
            SEMICONDUCTOR_DISPLAY_ENGINEER,
            PRODUCTION_WORKER

    )),

    EDUCATION("EDUCATION", "교육", EnumSet.of(
            INSTRUCTOR,
            LANGUAGE_EDUCATOR

    )),

    HEALTHCARE_PHARMA_BIO("HEALTHCARE_PHARMA_BIO", "의료/제약/바이오", EnumSet.of(
            BIOTECH_RESEARCHER,
            CLINICAL_RESEARCHER,
            MICROBIOLOGIST,
            HOSPITAL_COORDINATOR,
            PHARMACEUTICAL_CHEMIST,
            GENETIC_ENGINEER,
            CAREGIVER
    )),

    LOGISTICS_TRADE("LOGISTICS_TRADE", "물류/무역", EnumSet.of(
            LOGISTICS_MANAGER,
            LOGISTICS_ANALYST,
            EXPORT_IMPORT_OFFICER,
            TRADE_OFFICER,
            BUYER_MANAGER,
            AIR_TRANSPORT_AGENT,
            MARINE_TRANSPORT_AGENT,
            LOGISTICS_FIELD_WORKER

    )),

    FOOD_BEVERAGE("FOOD_BEVERAGE", "식/음료", EnumSet.of(
            FOOD_SERVICE_WORKER,
            CHEF,
            MENU_DEVELOPER,
            BARTENDER,
            SOMMELIER,
            FOOD_STYLIST
    )),

    CONSTRUCTION_FACILITIES("CONSTRUCTION_FACILITIES", "건설/시설", EnumSet.of(
            ARCHITECT,
            CONSTRUCTION_SUPERVISOR,
            MAINTENANCE_MANAGER,
            CONSTRUCTION_WORKER,
            WELDER,
            CARPENTER,
            HEAVY_EQUIPMENT_TECHNICIAN

    )),

    ENTERTAINMENT("ENTERTAINMENT", "엔터테인먼트", EnumSet.of(
            MODEL,
            ACTOR,
            SHOW_HOST

    )),


    ;
    private final String dbValue;
    private final String koreanName;
    private final Set<JobRole> roles;

    JobCategory(String dbValue,String koreanName, Set<JobRole> roles) {
        this.dbValue = dbValue;
        this.koreanName = koreanName;
        this.roles = roles;
    }


    public static JobCategory getCategoryByRole(JobRole role) {
        for (JobCategory category : JobCategory.values()) {
            if (category.roles.contains(role)) {
                return category;
            }
        }
        return null;
    }

    public static JobCategory getCategoryByDbValue(String dbValue) {
        for (JobCategory category : JobCategory.values()) {
            if (category.dbValue.equalsIgnoreCase(dbValue)) {
                return category;
            }
        }
        return null;
    }

    public static Set<JobRole> getRolesByCategory(JobCategory category) {
        return category.roles;
    }
}
