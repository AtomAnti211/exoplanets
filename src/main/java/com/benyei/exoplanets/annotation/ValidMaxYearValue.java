package com.benyei.exoplanets.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = ValidMaxYearValueValidator.class)
public @interface ValidMaxYearValue {
    String message() default "This did not happen in the future!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}
