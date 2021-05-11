package com.wufel.cleaning.robot.infrastructure.validator;


import com.wufel.cleaning.robot.domain.annotation.ArrayOfCoordinatesConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class InstructionArrayOfCoordinateValidator implements ConstraintValidator<ArrayOfCoordinatesConstraint, int[][]> {
    @Override
    public void initialize(ArrayOfCoordinatesConstraint arrayOfCoordinatesConstraint){}

    @Override
    public boolean isValid(int[][] coordinates, ConstraintValidatorContext cxt){
        long invalidCoordinates = Arrays.stream(coordinates).filter(coordinate -> coordinate.length != 2).count();
        return invalidCoordinates == 0;
    }
}
