package com.benyei.exoplanets;

import com.benyei.exoplanets.controller.PlanetController;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Detection;
import com.benyei.exoplanets.model.Planet;
import com.benyei.exoplanets.model.Star;
import com.benyei.exoplanets.service.PlanetService;
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

@WebMvcTest(PlanetController.class)
public class PlanetControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanetService planetService;

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Star STAR = new Star(1L, "DeathStar", 4444,
            77.7, 1.2, 1.1);

    @Test
    void getAllPlanets_shouldReturnAllPlanet() throws Exception {
        Planet planet1 = new Planet(
                1L, "Naboo", 1999, Detection.DIRECT_IMAGING,
                true, true, STAR);
        Planet planet2 = new Planet(2L, "Tatooine", 2002, Detection.ASTROMETRY,
                true, true, STAR);

        when(planetService.getAllPlanets()).thenReturn(List.of(planet1, planet2));

        mockMvc.perform(get("/api/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(planet1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(planet1.getName())))
                .andExpect(jsonPath("$[0].yearOfDiscovery", is(planet1.getYearOfDiscovery())))
                .andExpect(jsonPath("$[0].statusIsConfirmed", is(planet1.getStatusIsConfirmed())))
                .andExpect(jsonPath("$[0].inTheHabitableZone", is(planet1.getInTheHabitableZone())))
                .andExpect(jsonPath("$[1].id", is(planet2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(planet2.getName())))
                .andExpect(jsonPath("$[1].yearOfDiscovery", is(planet2.getYearOfDiscovery())))
                .andExpect(jsonPath("$[1].statusIsConfirmed", is(planet2.getStatusIsConfirmed())))
                .andExpect(jsonPath("$[1].inTheHabitableZone", is(planet2.getInTheHabitableZone())));
    }

    @Test
    void crateNewPlanet_shouldReturnSamePlanet() throws Exception {
        Planet newPlanet = new Planet(
                1L, "Dagobah", 1999, Detection.DIRECT_IMAGING,
                true, true, STAR);
        when(planetService.savePlanet(any())).thenReturn(newPlanet);

        mockMvc.perform(post("/api/planets", newPlanet)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(newPlanet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(newPlanet.getId().intValue())))
                .andExpect(jsonPath("$.name", is(newPlanet.getName())))
                .andExpect(jsonPath("$.yearOfDiscovery", is(newPlanet.getYearOfDiscovery())))
                .andExpect(jsonPath("$.statusIsConfirmed", is(newPlanet.getStatusIsConfirmed())))
                .andExpect(jsonPath("$.inTheHabitableZone", is(newPlanet.getInTheHabitableZone())));
    }

    @Test
    void findById_inputValidId_shouldReturnRightPlanet() throws Exception {
        Long id = 1L;
        Planet planet = new Planet(id, "Dagobah", 1999, Detection.DIRECT_IMAGING,
                true, true, STAR);
        when(planetService.getPlanetById(anyLong())).thenReturn(planet);
        mockMvc.perform(get("/api/planets/{id}", id))
                .andExpect(jsonPath("$.id", is(planet.getId().intValue())))
                .andExpect(jsonPath("$.name", is(planet.getName())))
                .andExpect(jsonPath("$.yearOfDiscovery", is(planet.getYearOfDiscovery())))
                .andExpect(jsonPath("$.statusIsConfirmed", is(planet.getStatusIsConfirmed())))
                .andExpect(jsonPath("$.inTheHabitableZone", is(planet.getInTheHabitableZone())));
    }

    @Test
    void updatePlanetById_shouldReturnUpdatedPlanet() throws Exception {
        Long id = 1L;
        Planet planet2 = new Planet(id, "Dagobah", 1999, Detection.DIRECT_IMAGING,
                true, true, STAR);
        when(planetService.updatePlanet(any(), anyLong())).thenReturn(planet2);
        mockMvc.perform(put("/api/planets/{id}", id, planet2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(planet2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(planet2.getId().intValue())))
                .andExpect(jsonPath("$.name", is(planet2.getName())))
                .andExpect(jsonPath("$.yearOfDiscovery", is(planet2.getYearOfDiscovery())))
                .andExpect(jsonPath("$.statusIsConfirmed", is(planet2.getStatusIsConfirmed())))
                .andExpect(jsonPath("$.inTheHabitableZone", is(planet2.getInTheHabitableZone())));
    }

    @Test
    void updateByIdWithInvalidYearOfDiscoveryPlanet_shouldReturnBadRequestStatus() throws Exception {
        Long id = 1L;
        Planet invalidPlanet = new Planet(id, "Bespin", 1980, Detection.DIRECT_IMAGING,
                true, true, STAR);
        mockMvc.perform(put("/api/planets/{id}", id, invalidPlanet)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidPlanet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById_inputValidId_shouldReturnNoContentStatus() throws Exception {
        doNothing().when(planetService).deletePlanet(anyLong());
        mockMvc.perform(delete("/api/planets/{id}", anyLong()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_inputNonExistentId_shouldReturnNotFoundStatus() throws Exception {
        long id = 1L;
        doThrow(new ResourceNotFoundException("With this ID: " + id + ", planet does not exist."))
                .when(planetService)
                .deletePlanet(anyLong());
        mockMvc.perform(delete("/api/planets/{id}", anyLong()))
                .andExpect(status().isNotFound());
    }
}

