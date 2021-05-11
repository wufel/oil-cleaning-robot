package com.wufel.cleaning.robot.application;

import com.wufel.cleaning.robot.domain.entity.CleaningInstruction;
import com.wufel.cleaning.robot.domain.entity.CleaningOutput;
import com.wufel.cleaning.robot.domain.entity.Coordinate;
import com.wufel.cleaning.robot.domain.entity.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wufel.cleaning.robot.infrastructure.util.Utils.checkBoundary;

public class OilCleaningRobot {
    public static Logger LOG = LoggerFactory.getLogger(OilCleaningRobot.class);

    private Coordinate currentPosition;
    private Integer oilPatchesCleaned;
    private final Coordinate boundary;
    private Set<Coordinate> oilPatches;
    private final String navigationInstructions;

    public OilCleaningRobot(CleaningInstruction instruction) {
        currentPosition = new Coordinate(instruction.getStartingPosition());
        boundary = new Coordinate(instruction.getAreaSize());
        navigationInstructions = instruction.getNavigationInstructions();
        oilPatchesCleaned = 0;
        //assumption on there will be no duplicated oil patches provided
        oilPatches = Arrays.stream(instruction.getOilPatches())
                .map(Coordinate::new)
                .collect(Collectors.toSet());
        checkBoundary(currentPosition, boundary);
    }

    public CleaningOutput navigateAndClean() {
        LOG.info("Begin to move and clean");
        Stream.of(navigationInstructions.split(""))
                .map(Direction::valueOf)
                .map(this::move)
                .forEach(this::clean);

        CleaningOutput output = new CleaningOutput(currentPosition.toArray(), oilPatchesCleaned);
        LOG.info("Cleaning finished with final position {}, patch cleaned {}, patch left uncleaned {}",
                output.getFinalPosition(), output.getOilPatchesCleaned(), oilPatches.size());
        return output;
    }

    private Coordinate move(Direction direction) {
        Coordinate movedCoordinate = new Coordinate(currentPosition.getX() + direction.getDirectionX(),
                currentPosition.getY() + direction.getDirectionY());
        checkBoundary(movedCoordinate, boundary);
        return movedCoordinate;
    }

    private void clean(Coordinate coordinate) {
        currentPosition = coordinate;
        if (oilPatches.contains(coordinate)) {
            LOG.info("cleaning patch {}", coordinate);
            oilPatches.remove(coordinate);
            oilPatchesCleaned++;
        }
    }
}
