package com.benyei.exoplanets.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

public class ValidMaxYearValueValidator implements ConstraintValidator<ValidMaxYearValue,Integer> {
    @Override
    public void initialize(ValidMaxYearValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer n, ConstraintValidatorContext constraintValidatorContext) {
        return n <= Calendar.getInstance().get(Calendar.YEAR);
    }
}
