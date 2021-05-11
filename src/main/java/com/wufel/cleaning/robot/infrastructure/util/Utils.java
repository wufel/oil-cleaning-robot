package com.wufel.cleaning.robot.infrastructure.util;

import com.wufel.cleaning.robot.domain.entity.Coordinate;
import com.wufel.cleaning.robot.domain.exception.OutOfCleaningBoundaryException;

public final class Utils {

    public static void checkBoundary(Coordinate location, Coordinate boundary) {
        if (boundary.getX() <= location.getX()
                || boundary.getY() <= location.getY()
                || location.getX() < 0
                || location.getY() < 0) {
            throw new OutOfCleaningBoundaryException(String.format("location %s is out of cleaning boundary", location.toString()));
        }
    }
}
