package com.wufel.cleaning.robot.domain.entity;

public class Coordinate extends Entity {
    private final int x;
    private final int y;

    public Coordinate(int[] coordinates) {
        this.x = coordinates[0];
        this.y = coordinates[1];
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] toArray() {
        return new int[]{this.x, this.y};
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
