package com.wufel.cleaning.robot.domain.entity;


import com.wufel.cleaning.robot.domain.annotation.ArrayOfCoordinatesConstraint;
import com.wufel.cleaning.robot.domain.annotation.CoordinateConstraint;
import com.wufel.cleaning.robot.domain.annotation.NavigationStringConstraint;

public class CleaningInstruction extends Entity {
    @CoordinateConstraint
    private int[] areaSize;
    @CoordinateConstraint
    private int[] startingPosition;
    @ArrayOfCoordinatesConstraint
    private int[][] oilPatches;
    @NavigationStringConstraint
    private String navigationInstructions;

    private CleaningInstruction() {
    }

    public CleaningInstruction(int[] areaSize,
                               int[] startingPosition,
                               int[][] oilPatches,
                               String navigationInstructions) {
        this.areaSize = areaSize;
        this.startingPosition = startingPosition;
        this.oilPatches = oilPatches;
        this.navigationInstructions = navigationInstructions;
    }

    public int[] getAreaSize() {
        return areaSize;
    }

    public int[] getStartingPosition() {
        return startingPosition;
    }

    public int[][] getOilPatches() {
        return oilPatches;
    }

    public String getNavigationInstructions() {
        return navigationInstructions;
    }
}
