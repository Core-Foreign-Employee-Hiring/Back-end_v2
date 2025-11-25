package com.forwork.backend.common.validation;

import com.forwork.backend.api.recruit.enums.ContractType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ContractTypeValidator.class)
public @interface ValidContractType   {

    String message() default "허용되지 않은 계약 형태입니다.";
    Class<?>[] groups() default {};
    Class<?extends Payload>[] payload() default {};

    ContractType[] anyOf();

}
