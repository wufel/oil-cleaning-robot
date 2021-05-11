package com.wufel.cleaning.robot.application;

import com.wufel.cleaning.robot.domain.entity.CleaningInstruction;
import com.wufel.cleaning.robot.domain.entity.CleaningOutput;
import com.wufel.cleaning.robot.domain.entity.Coordinate;
import com.wufel.cleaning.robot.domain.entity.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wufel.cleaning.robot.infrastructure.util.Utils.checkBoundary;

@Service
public class OilCleaningService {

    public static Logger LOG = LoggerFactory.getLogger(OilCleaningService.class);

    public CleaningOutput navigateAndClean(CleaningInstruction instruction) {
        LOG.info("Preparing cleaning process...");
        Coordinate currentPosition = new Coordinate(instruction.getStartingPosition());
        CleaningOutput output = new CleaningOutput(currentPosition.toArray());
        Coordinate boundary = new Coordinate(instruction.getAreaSize());

        //assumption on there will be no duplicated oil patches provided
        Set<Coordinate> oilPatches = Arrays.stream(instruction.getOilPatches())
                .map(Coordinate::new)
                .collect(Collectors.toSet());
        checkBoundary(currentPosition, boundary);

        LOG.info("Begin to move and clean");
        Stream.of(instruction.getNavigationInstructions().split(""))
                .map(Direction::valueOf)
                .map(direction -> move(output, boundary, direction))
                .forEach(location -> clean(location, oilPatches, output));

        LOG.info("Cleaning finished with final position {}, patch cleaned {}, patch left uncleaned {}",
                output.getFinalPosition(), output.getOilPatchesCleaned(), oilPatches.size());
        return output;
    }

    private Coordinate move(CleaningOutput output, Coordinate boundary, Direction direction) {
        Coordinate originalCoordinate = new Coordinate(output.getFinalPosition());
        Coordinate movedCoordinate = new Coordinate(originalCoordinate.getX() + direction.getDirectionX(),
                originalCoordinate.getY() + direction.getDirectionY());
        checkBoundary(movedCoordinate, boundary);
        return movedCoordinate;
    }

    private void clean(Coordinate coordinate, Set<Coordinate> oilPatches, CleaningOutput output) {
        output.setFinalPosition(coordinate.toArray());
        if (oilPatches.contains(coordinate)) {
            LOG.info("cleaning patch {}", coordinate);
            oilPatches.remove(coordinate);
            output.incrementOilPatchesCleaned();
        }
    }

}
