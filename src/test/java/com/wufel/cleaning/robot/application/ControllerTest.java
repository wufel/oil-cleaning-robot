package com.wufel.cleaning.robot.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wufel.cleaning.robot.domain.entity.CleaningOutput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
class ControllerTest {

    private static final MockHttpServletRequestBuilder REQUEST_BUILDER =
            request(HttpMethod.POST, "/instructions")
                    .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON);

    private static final MockHttpServletRequestBuilder CLEAN_REQUEST_BUILDER =
            request(HttpMethod.GET, "/clean")
                    .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON);

    private static final MockHttpServletRequestBuilder CLEAN_WITH_ROBOT_REQUEST_BUILDER =
            request(HttpMethod.GET, "/robotClean")
                    .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON);

    @Autowired
    private MockMvc mockMvc;

    @Test
    void ensuresCorrectEndpointIsExposed() throws Exception {
        mockMvc.perform(REQUEST_BUILDER.content(BASIC_CASE_REQUEST))
                .andExpect(status().isOk());
    }

    private static final String BASIC_CASE_REQUEST =
            "{\"areaSize\": [5, 5], \"startingPosition\": [1, 2], \"oilPatches\": [[1, 0], [2, 2], [2, 3]], \"navigationInstructions\": \"NNESEESWNWW\"}";

    @Test
    void ensuresCleanEndpointCleans() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        CleaningOutput expectedOutput = new CleaningOutput(new int[]{1, 3}, 1);
        String expectedOutputString = objectMapper.writeValueAsString(expectedOutput);
        mockMvc.perform(CLEAN_REQUEST_BUILDER.content(BASIC_CASE_REQUEST))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutputString));
    }

    @Test
    void testRobotCleanReturnSameResult() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        CleaningOutput expectedOutput = new CleaningOutput(new int[]{1, 3}, 1);
        String expectedOutputString = objectMapper.writeValueAsString(expectedOutput);
        mockMvc.perform(CLEAN_WITH_ROBOT_REQUEST_BUILDER.content(BASIC_CASE_REQUEST))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutputString));
    }
}