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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.wufel.cleaning.robot.domain.entity.Direction.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OilCleaningServiceTest {

    private static final String BASIC_CASE_REQUEST =
            "{\"areaSize\": [5, 5], \"startingPosition\": [1, 2], \"oilPatches\": [[1, 0], [2, 2], [2, 3]], \"navigationInstructions\": \"NNESEESWNWW\"}";
    private static final String OUT_OF_BOUNDARY_CASE_REQUEST =
            "{\"areaSize\": [5, 5], \"startingPosition\": [1, 2], \"oilPatches\": [[1, 0], [2, 2], [2, 3]], \"navigationInstructions\": \"EEEE\"}";

    private static ObjectMapper objectMapper;
    private static OilCleaningService service;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        service = new OilCleaningService();
    }

    @Test
    public void navigateAndCleanProperly() throws JsonProcessingException {
        CleaningInstruction instruction = objectMapper.readValue(BASIC_CASE_REQUEST, CleaningInstruction.class);
        CleaningOutput expectedOutput = new CleaningOutput(new int[]{1, 3}, 1);

        CleaningOutput cleaningOutput = service.navigateAndClean(instruction);
        assertEquals(expectedOutput, cleaningOutput);
    }

    @Test
    public void cleaningOutOfBoundary() throws JsonProcessingException {
        CleaningInstruction instruction = objectMapper.readValue(OUT_OF_BOUNDARY_CASE_REQUEST, CleaningInstruction.class);
        assertThrows(OutOfCleaningBoundaryException.class, () -> service.navigateAndClean(instruction));
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
        Coordinate boundary = new Coordinate(100, 100);
        CleaningOutput output = new CleaningOutput(initial.toArray(), 0);
        Coordinate actual = service.move(output, boundary, direction);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> cleanCorrectlyParams() {
        return Stream.of(
                Arguments.of(new Coordinate(1, 1), new HashSet<>(List.of(new Coordinate(1, 1))), 1, 0),
                Arguments.of(new Coordinate(1, 1), new HashSet<>(List.of(new Coordinate(1, 2))), 0, 1),
                Arguments.of(new Coordinate(1, 1), new HashSet<>(List.of(new Coordinate(1, 1), new Coordinate(2, 2))), 1, 1),
                Arguments.of(new Coordinate(1, 1), new HashSet<>(List.of()), 0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("cleanCorrectlyParams")
    public void cleanCorrectly(Coordinate location, Set<Coordinate> oilPatches, int expectedPatchesCleaned, int expectedPatchesLeft) {
        CleaningOutput output = new CleaningOutput(location.toArray(), 0);
        service.clean(location, oilPatches, output);
        assertEquals(expectedPatchesCleaned, output.getOilPatchesCleaned());
        assertEquals(expectedPatchesLeft, oilPatches.size());
    }

    private static Stream<Arguments> boundaryTestParams() {
        return Stream.of(
                Arguments.of(new Coordinate(5, 4), new Coordinate(5, 5)),
                Arguments.of(new Coordinate(1, 5), new Coordinate(5, 5)),
                Arguments.of(new Coordinate(5, -1), new Coordinate(5, 5)),
                Arguments.of(new Coordinate(-100, 4), new Coordinate(5, 5))
        );
    }

    @ParameterizedTest
    @MethodSource("boundaryTestParams")
    public void boundaryCheckTest(Coordinate location, Coordinate boundary) {
        assertThrows(OutOfCleaningBoundaryException.class, () -> service.checkBoundary(location, boundary));
    }

}
