package com.forwork.backend.common.validation;

import com.forwork.backend.api.recruit.enums.ContractType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class ContractTypeValidator implements ConstraintValidator<ValidContractType, ContractType> {

    private Set<ContractType> allowedValues;

    @Override
    public void initialize(ValidContractType constraintAnnotation) {
        allowedValues = Set.of(constraintAnnotation.anyOf());
    }

    @Override
    public boolean isValid(ContractType value, ConstraintValidatorContext context) {
        return value == null || allowedValues.contains(value);
    }
}


