package com.wufel.cleaning.robot.domain.annotation;


import com.wufel.cleaning.robot.infrastructure.validator.InstructionCoordinateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InstructionCoordinateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CoordinateConstraint {
    String message() default "Array an Invalid Coordinate";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
