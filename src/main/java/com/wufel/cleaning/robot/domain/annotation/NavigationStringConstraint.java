package com.wufel.cleaning.robot.domain.annotation;


import com.wufel.cleaning.robot.infrastructure.validator.InstructionNavigationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InstructionNavigationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NavigationStringConstraint {

    String message() default "Invalid Navigation String";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
