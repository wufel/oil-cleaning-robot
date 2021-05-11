package com.wufel.cleaning.robot.application;

import com.wufel.cleaning.robot.domain.entity.CleaningInstruction;
import com.wufel.cleaning.robot.domain.entity.CleaningOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class Controller {

    public static Logger LOG = LoggerFactory.getLogger(Controller.class);

    @Autowired
    OilCleaningService cleaningService;

    @PostMapping("/instructions")
    public void basicInstructions(@RequestBody CleaningInstruction instruction) {
        LOG.info("Instruction received {}", instruction);
    }

    @GetMapping("/clean")
    public CleaningOutput navigateAndClean(@Valid @RequestBody CleaningInstruction instruction) {
        return cleaningService.navigateAndClean(instruction);
    }

    @GetMapping("/robotClean")
    public CleaningOutput navigateAndCleanByRobot(@Valid @RequestBody CleaningInstruction instruction) {
        OilCleaningRobot oilCleaningRobot = new OilCleaningRobot(instruction);
        return oilCleaningRobot.navigateAndClean();
    }

}
