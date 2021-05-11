package com.wufel.cleaning.robot.domain.entity;

import java.util.Arrays;
import java.util.Objects;

public enum Direction {
    N(0, 1),
    S(0, -1),
    E(1, 0),
    W(-1, 0);

    private int directionX;
    private int directionY;

    Direction(int directionX, int directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public static boolean isValueOf(String letter){
        long count = Arrays.stream(Direction.values()).filter(direction -> direction.name().equals(letter)).count();
        return !Objects.equals(count, 0L);
    }

    public int getDirectionX() {
        return directionX;
    }

    public int getDirectionY() {
        return directionY;
    }
}
