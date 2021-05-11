package com.wufel.cleaning.robot.application;

import com.wufel.cleaning.robot.domain.entity.Coordinate;
import com.wufel.cleaning.robot.domain.exception.OutOfCleaningBoundaryException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.wufel.cleaning.robot.infrastructure.util.Utils.checkBoundary;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CleaningUtilsTest {

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
        assertThrows(OutOfCleaningBoundaryException.class, () -> checkBoundary(location, boundary));
    }

}
