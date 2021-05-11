package com.wufel.cleaning.robot.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wufel.cleaning.robot.application.Controller;
import com.wufel.cleaning.robot.domain.entity.CleaningInstruction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
public class ControllerExceptionHandlerTest {

    private static final MockHttpServletRequestBuilder CLEAN_REQUEST_BUILDER =
            request(HttpMethod.GET, "/clean")
                    .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON);

    private static final String INVALID_NAVITGATION_ERROR_MESSAGE = "[{\"field\":\"navigationInstructions\",\"message\":\"Invalid Navigation String\"}]";
    private static final String INVALID_COORDINATE_ERROR_MESSAGE = "\"message\":\"Array an Invalid Coordinate\"";
    private static final String INVALID_OIL_PATCHES_ERROR_MESSAGE = "[{\"field\":\"oilPatches\",\"message\":\"Item In Array Not a Valid Coordination\"}]";
    public static final String VALID_CLEAN_OUTPUT = "{\n" +
            "  \"finalPosition\" : [1, 3],\n" +
            "  \"oilPatchesCleaned\" : 1\n" +
            "}";

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
    }

    private static Stream<Arguments> inValidNavigationParams() {
        return Stream.of(
                Arguments.of("ABCDEFG"),
                Arguments.of("|!@#$"),
                Arguments.of("E "),
                Arguments.of("E SW"),
                Arguments.of("nsew")
        );
    }

    @ParameterizedTest
    @MethodSource("inValidNavigationParams")
    public void invalidNavigationErrorTest(String instructions) throws Exception {
        CleaningInstruction cleaningInstruction = new CleaningInstruction(new int[]{2, 2}, new int[]{1, 1}, new int[][]{{1, 1}}, instructions);
        String cleaningInstructionString = mapper.writeValueAsString(cleaningInstruction);

        mockMvc.perform(CLEAN_REQUEST_BUILDER.content(cleaningInstructionString))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(INVALID_NAVITGATION_ERROR_MESSAGE));
    }

    private static Stream<Arguments> inValidCoordinateParams() {
        return Stream.of(
                Arguments.of(new int[]{}, new int[]{1, 1}, new int[][]{{1, 1}}),
                Arguments.of(new int[]{1}, new int[]{1, 1}, new int[][]{{1, 1}}),
                Arguments.of(new int[]{1, 1, 1}, new int[]{1, 1}, new int[][]{{1, 1}}),
                Arguments.of(new int[]{1, 1}, new int[]{}, new int[][]{{1, 1}}),
                Arguments.of(new int[]{1, 1}, new int[]{1}, new int[][]{{1, 1}}),
                Arguments.of(new int[]{1, 1}, new int[]{1, 1, 1}, new int[][]{{1, 1}})
        );
    }

    @ParameterizedTest
    @MethodSource("inValidCoordinateParams")
    public void invalidCoordinateTest(int[] areaSize, int[] startingPosition, int[][] oilPatches) throws Exception {
        CleaningInstruction cleaningInstruction = new CleaningInstruction(areaSize, startingPosition, oilPatches, "N");
        String cleaningInstructionString = mapper.writeValueAsString(cleaningInstruction);

        mockMvc.perform(CLEAN_REQUEST_BUILDER.content(cleaningInstructionString))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(INVALID_COORDINATE_ERROR_MESSAGE)));
    }

    private static Stream<Arguments> inValidCoordinateArraysParams() {
        return Stream.of(
                Arguments.of(new int[]{1, 1}, new int[]{1, 1}, new int[][]{{}}),
                Arguments.of(new int[]{1, 1}, new int[]{1, 1}, new int[][]{{1}}),
                Arguments.of(new int[]{1, 1}, new int[]{1, 1}, new int[][]{{1, 1, 1}}),
                Arguments.of(new int[]{1, 1}, new int[]{1, 1}, new int[][]{{1, 1}, {}}),
                Arguments.of(new int[]{1, 1}, new int[]{1, 1}, new int[][]{{1, 1}, {1}}),
                Arguments.of(new int[]{1, 1}, new int[]{1, 1}, new int[][]{{1, 1}, {1, 1, 1}})
        );
    }

    @ParameterizedTest
    @MethodSource("inValidCoordinateArraysParams")
    public void invalidArrayOfCoordinatesTest(int[] areaSize, int[] startingPosition, int[][] oilPatches) throws Exception {
        CleaningInstruction cleaningInstruction = new CleaningInstruction(areaSize, startingPosition, oilPatches, "N");
        String cleaningInstructionString = mapper.writeValueAsString(cleaningInstruction);

        mockMvc.perform(CLEAN_REQUEST_BUILDER.content(cleaningInstructionString))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(INVALID_OIL_PATCHES_ERROR_MESSAGE));
    }

    @Test
    public void validatePass() throws Exception {
        CleaningInstruction cleaningInstruction = new CleaningInstruction(
                new int[]{5, 5},
                new int[]{1, 2},
                new int[][]{{1, 0}, {2, 2}, {2, 3}},
                "NNESEESWNWW");
        String cleaningInstructionString = mapper.writeValueAsString(cleaningInstruction);

        mockMvc.perform(CLEAN_REQUEST_BUILDER.content(cleaningInstructionString))
                .andExpect(status().isOk())
                .andExpect(content().json(VALID_CLEAN_OUTPUT));

    }
}
