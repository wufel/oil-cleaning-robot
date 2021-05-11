package com.wufel.cleaning.robot.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wufel.cleaning.robot.domain.entity.CleaningInstruction;
import com.wufel.cleaning.robot.domain.entity.CleaningOutput;
import com.wufel.cleaning.robot.domain.entity.Coordinate;
import com.wufel.cleaning.robot.domain.entity.Direction;
import com.wufel.cleaning.robot.domain.exception.OutOfCleaningBoundaryException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wufel.cleaning.robot.domain.entity.Direction.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OilCleaningRobotTest {

    private static final String BASIC_CASE_REQUEST =
            "{\"areaSize\": [5, 5], \"startingPosition\": [1, 2], \"oilPatches\": [[1, 0], [2, 2], [2, 3]], \"navigationInstructions\": \"NNESEESWNWW\"}";
    private static final String OUT_OF_BOUNDARY_CASE_REQUEST =
            "{\"areaSize\": [5, 5], \"startingPosition\": [1, 2], \"oilPatches\": [[1, 0], [2, 2], [2, 3]], \"navigationInstructions\": \"EEEE\"}";

    private static ObjectMapper objectMapper;
    private static OilCleaningService service;
    private static int[] boundary;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        service = new OilCleaningService();
        boundary = new int[]{100, 100};
    }

    @Test
    public void navigateAndCleanProperly() throws JsonProcessingException {
        CleaningInstruction instruction = objectMapper.readValue(BASIC_CASE_REQUEST, CleaningInstruction.class);
        CleaningOutput expectedOutput = new CleaningOutput(new int[]{1, 3}, 1);
        OilCleaningRobot oilCleaningRobot = new OilCleaningRobot(instruction);
        CleaningOutput cleaningOutput = oilCleaningRobot.navigateAndClean();
        assertEquals(expectedOutput, cleaningOutput);
    }

    @Test
    public void cleaningOutOfBoundary() throws JsonProcessingException {
        CleaningInstruction instruction = objectMapper.readValue(OUT_OF_BOUNDARY_CASE_REQUEST, CleaningInstruction.class);
        OilCleaningRobot oilCleaningRobot = new OilCleaningRobot(instruction);
        assertThrows(OutOfCleaningBoundaryException.class, oilCleaningRobot::navigateAndClean);
    }

    private static Stream<Arguments> moveCorrectlyParams() {
        return Stream.of(
                Arguments.of(N, new Coordinate(1, 1), new Coordinate(1, 2)),
                Arguments.of(E, new Coordinate(1, 1), new Coordinate(2, 1)),
                Arguments.of(S, new Coordinate(1, 1), new Coordinate(1, 0)),
                Arguments.of(W, new Coordinate(1, 1), new Coordinate(0, 1))
        );
    }

    @ParameterizedTest
    @MethodSource("moveCorrectlyParams")
    public void moveCorrectly(Direction direction, Coordinate initial, Coordinate expected) {
        CleaningInstruction cleaningInstruction = new CleaningInstruction(boundary, initial.toArray(), new int[1][2], direction.toString());
        OilCleaningRobot oilCleaningRobot = new OilCleaningRobot(cleaningInstruction);
        CleaningOutput cleaningOutput = oilCleaningRobot.navigateAndClean();
        Coordinate actual = new Coordinate(cleaningOutput.getFinalPosition());
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> cleanCorrectlyParams() {
        return Stream.of(
                Arguments.of(new Coordinate(0, 1), new HashSet<>(List.of(new Coordinate(1, 1))), 1, 0),
                Arguments.of(new Coordinate(0, 1), new HashSet<>(List.of(new Coordinate(1, 2))), 0, 1),
                Arguments.of(new Coordinate(0, 1), new HashSet<>(List.of(new Coordinate(1, 1), new Coordinate(2, 2))), 1, 1),
                Arguments.of(new Coordinate(0, 1), new HashSet<>(List.of()), 0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("cleanCorrectlyParams")
    public void cleanCorrectly(Coordinate location, Set<Coordinate> oilPatches, int expectedPatchesCleaned) {
        int[][] oilPatchesArray = new int[oilPatches.size()][];
        oilPatches.stream().map(Coordinate::toArray).collect(Collectors.toList()).toArray(oilPatchesArray);
        CleaningInstruction cleaningInstruction = new CleaningInstruction(boundary, location.toArray(), oilPatchesArray, E.toString());

        OilCleaningRobot oilCleaningRobot = new OilCleaningRobot(cleaningInstruction);
        CleaningOutput cleaningOutput = oilCleaningRobot.navigateAndClean();
        assertEquals(expectedPatchesCleaned, cleaningOutput.getOilPatchesCleaned());
    }
}
