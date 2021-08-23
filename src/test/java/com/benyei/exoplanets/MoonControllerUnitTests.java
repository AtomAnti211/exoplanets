package com.benyei.exoplanets;

import com.benyei.exoplanets.controller.MoonController;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Detection;
import com.benyei.exoplanets.model.Moon;
import com.benyei.exoplanets.model.Planet;
import com.benyei.exoplanets.model.Star;
import com.benyei.exoplanets.service.MoonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoonController.class)
public class MoonControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoonService moonService;

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Star STAR = new Star(1L, "DeathStar", 4444,
            77.7, 1.2, 1.1);
    private static final Planet PLANET = new Planet(1L, "Coruscant", 1999,
            Detection.DIRECT_IMAGING, true, true, STAR);

    @Test
    void getAllMoons_shouldReturnAllMoon() throws Exception {
        Moon moon1 = new Moon(1L, "Yavin IV.", true, PLANET);
        Moon moon2 = new Moon(2L, "Jedha", true, PLANET);

        when(moonService.getAllMoons()).thenReturn(List.of(moon1, moon2));

        mockMvc.perform(get("/api/moons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(moon1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(moon1.getName())))
                .andExpect(jsonPath("$[0].status", is(moon1.getStatus())))
                .andExpect(jsonPath("$[1].id", is(moon2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(moon2.getName())))
                .andExpect(jsonPath("$[1].status", is(moon2.getStatus())));
    }

    @Test
    void crateNewMoon_shouldReturnSameMoon() throws Exception {
        Moon newMoon = new Moon(1L, "Yavin IV.", true, PLANET);
        when(moonService.saveMoon(any())).thenReturn(newMoon);

        mockMvc.perform(post("/api/moons", newMoon)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(newMoon)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(newMoon.getId().intValue())))
                .andExpect(jsonPath("$.name", is(newMoon.getName())))
                .andExpect(jsonPath("$.status", is(newMoon.getStatus())));
    }

    @Test
    void findById_inputValidId_shouldReturnRightMoon() throws Exception {
        Long id = 1L;
        Moon moon = new Moon(id, "Yavin IV.", true, PLANET);
        when(moonService.getMoonById(anyLong())).thenReturn(moon);
        mockMvc.perform(get("/api/moons/{id}", id))
                .andExpect(jsonPath("$.id", is(moon.getId().intValue())))
                .andExpect(jsonPath("$.name", is(moon.getName())))
                .andExpect(jsonPath("$.status", is(moon.getStatus())));
    }

    @Test
    void updateMoonById_shouldReturnUpdatedMoon() throws Exception {
        Long id = 1L;
        Moon moon2 = new Moon(id, "Yavin IV.", true, PLANET);
        when(moonService.updateMoon(any(), anyLong())).thenReturn(moon2);
        mockMvc.perform(put("/api/moons/{id}", id, moon2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(moon2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(moon2.getId().intValue())))
                .andExpect(jsonPath("$.name", is(moon2.getName())));
    }

    @Test
    void deleteById_inputValidId_shouldReturnNoContentStatus() throws Exception {
        doNothing().when(moonService).deleteMoon(anyLong());
        mockMvc.perform(delete("/api/moons/{id}", anyLong()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_inputNonExistentId_shouldReturnNotFoundStatus() throws Exception {
        long id = 1L;
        doThrow(new ResourceNotFoundException("With this ID: " + id + ", moon does not exist."))
                .when(moonService)
                .deleteMoon(anyLong());
        mockMvc.perform(delete("/api/moons/{id}", anyLong()))
                .andExpect(status().isNotFound());
    }
}
