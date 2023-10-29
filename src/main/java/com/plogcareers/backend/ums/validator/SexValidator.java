package com.plogcareers.backend.ums.validator;

import com.plogcareers.backend.ums.annotation.ValidSex;
import com.plogcareers.backend.ums.domain.entity.Sex;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SexValidator implements ConstraintValidator<ValidSex, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.equals(Sex.MALE.name()) || value.equals(Sex.FEMALE.name());
    }
}

