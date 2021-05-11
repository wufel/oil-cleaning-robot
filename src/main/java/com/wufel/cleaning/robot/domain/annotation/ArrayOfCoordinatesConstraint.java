package com.wufel.cleaning.robot.domain.annotation;


import com.wufel.cleaning.robot.infrastructure.validator.InstructionArrayOfCoordinateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InstructionArrayOfCoordinateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayOfCoordinatesConstraint {
    String message() default "Item In Array Not a Valid Coordination";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
