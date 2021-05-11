package com.wufel.cleaning.robot.domain.entity;

public class CleaningOutput extends Entity{

    private int[] finalPosition;
    private int oilPatchesCleaned;

    private CleaningOutput() {
    }

    public CleaningOutput(int[]  position) {
        this.finalPosition = position;
        oilPatchesCleaned = 0;
    }

    public CleaningOutput(int[] finalPosition, int oilPatchesCleaned) {
        this.finalPosition = finalPosition;
        this.oilPatchesCleaned = oilPatchesCleaned;
    }

    public void incrementOilPatchesCleaned() {
        this.oilPatchesCleaned++;
    }

    public int[] getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(int[] finalPosition) {
        this.finalPosition = finalPosition;
    }

    public int getOilPatchesCleaned() {
        return oilPatchesCleaned;
    }

}
