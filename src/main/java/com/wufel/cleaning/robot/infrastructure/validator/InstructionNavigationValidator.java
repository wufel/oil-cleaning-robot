package com.wufel.cleaning.robot.infrastructure.validator;

import com.wufel.cleaning.robot.domain.annotation.NavigationStringConstraint;
import com.wufel.cleaning.robot.domain.entity.Direction;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

@Component
public class InstructionNavigationValidator implements ConstraintValidator<NavigationStringConstraint, String> {

    @Override
    public void initialize(NavigationStringConstraint navigationStringConstraint){}

    @Override
    public boolean isValid(String navigation, ConstraintValidatorContext cxt){
        long invalidNavigation = Arrays.stream(navigation.split(""))
                .filter(insChar -> !Direction.isValueOf(insChar))
                .count();
        return Objects.equals(invalidNavigation, 0L);
    }
}
