package com.duyle.application_server;

import com.duyle.application_server.model.PointModification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {ApplicationServer.class})
@AutoConfigureMockMvc
class PointsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testPointModificationApi() throws Exception {
        PointModification request = PointModification.builder()
                .gameId(1L)
                .gameName("Hello World")
                .gamerId(2L)
                .pointsChanged(20L)
                .build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/points")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        PointModification response = objectMapper.readValue(result.getResponse().getContentAsString(), PointModification.class);
        assertEquals(request.getGameId(), response.getGameId());
        assertEquals(request.getGameName(), response.getGameName());
        assertEquals(request.getPointsChanged(), response.getPointsChanged());
        assertEquals(request.getGamerId(), response.getGamerId());

        request = PointModification.builder()
                .gameName("Hello World")
                .gamerId(2L)
                .pointsChanged(20L)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/points")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}
