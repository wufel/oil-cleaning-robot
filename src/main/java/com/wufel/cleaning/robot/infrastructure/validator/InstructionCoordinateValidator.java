package com.wufel.cleaning.robot.infrastructure.validator;

import com.wufel.cleaning.robot.domain.annotation.CoordinateConstraint;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class InstructionCoordinateValidator implements ConstraintValidator<CoordinateConstraint, int[]> {

    @Override
    public void initialize(CoordinateConstraint coordinateConstraint){}

    @Override
    public boolean isValid(int[] coordination, ConstraintValidatorContext cxt){
        return coordination.length == 2;
    }

}
