package com.wufel.cleaning.robot.domain.exception;

public class OutOfCleaningBoundaryException extends RuntimeException {

    public OutOfCleaningBoundaryException(String message) {
        super(message);
    }
}
