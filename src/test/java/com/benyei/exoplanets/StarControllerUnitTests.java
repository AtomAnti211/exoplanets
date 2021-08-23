package com.benyei.exoplanets;

import com.benyei.exoplanets.controller.StarController;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Star;
import com.benyei.exoplanets.service.StarService;
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


@WebMvcTest(StarController.class)
public class StarControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StarService starService;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllStars_shouldReturnAllStar() throws Exception {
        Star star1 = new Star(1L, "Opportunity", 4567, 78.7, 1.3, 1.4);
        Star star2 = new Star(2L, "Csu-zsung", 4568, 178.8, 0.15, 0.16);

        when(starService.getAllStars()).thenReturn(List.of(star1, star2));

        mockMvc.perform(get("/api/stars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(star1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(star1.getName())))
                .andExpect(jsonPath("$[0].temperature", is(star1.getTemperature())))
                .andExpect(jsonPath("$[0].distanceInLightYears", is(star1.getDistanceInLightYears())))
                .andExpect(jsonPath("$[0].radiusSun", is(star1.getRadiusSun())))
                .andExpect(jsonPath("$[0].massSun", is(star1.getMassSun())))
                .andExpect(jsonPath("$[1].id", is(star2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(star2.getName())))
                .andExpect(jsonPath("$[1].temperature", is(star2.getTemperature())))
                .andExpect(jsonPath("$[1].distanceInLightYears", is(star2.getDistanceInLightYears())))
                .andExpect(jsonPath("$[1].radiusSun", is(star2.getRadiusSun())))
                .andExpect(jsonPath("$[1].massSun", is(star2.getMassSun())));
    }

    @Test
    void crateNewStar_shouldReturnSameStar() throws Exception {
        Star newStar = new Star(1L, "Opportunity", 4567, 78.7, 1.3, 1.4);
        when(starService.saveStar(any())).thenReturn(newStar);

        mockMvc.perform(post("/api/stars", newStar)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(newStar)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newStar.getName())))
                .andExpect(jsonPath("$.temperature", is(newStar.getTemperature())))
                .andExpect((jsonPath("$.distanceInLightYears", is(newStar.getDistanceInLightYears()))))
                .andExpect(jsonPath("$.radiusSun", is(newStar.getRadiusSun())))
                .andExpect(jsonPath("$.massSun", is(newStar.getMassSun())));
    }

    @Test
    void findById_inputValidId_shouldReturnRightStar() throws Exception {
        Long id = 1L;
        Star star = new Star(id, "Opportunity", 4567, 78.7, 1.3, 1.4);
        when(starService.getStarById(anyLong())).thenReturn(star);
        mockMvc.perform(get("/api/stars/{id}", id))
                .andExpect(jsonPath("$.id", is(star.getId().intValue())))
                .andExpect(jsonPath("$.name", is(star.getName())))
                .andExpect(jsonPath("$.temperature", is(star.getTemperature())))
                .andExpect(jsonPath("$.distanceInLightYears", is(star.getDistanceInLightYears())))
                .andExpect(jsonPath("$.radiusSun", is(star.getRadiusSun())))
                .andExpect(jsonPath("$.massSun", is(star.getMassSun())));
    }

    @Test
    void updateStarById_shouldReturnUpdatedStar() throws Exception {
        Long id = 1L;
        Star star2 = new Star(id, "Updated name", 5567, 728.7, 5.3, 5.8);
        when(starService.updateStar(any(), anyLong())).thenReturn(star2);
        mockMvc.perform(put("/api/stars/{id}", id, star2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(star2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(star2.getId().intValue())))
                .andExpect(jsonPath("$.name", is(star2.getName())))
                .andExpect(jsonPath("$.temperature", is(star2.getTemperature())))
                .andExpect(jsonPath("$.distanceInLightYears", is(star2.getDistanceInLightYears())))
                .andExpect(jsonPath("$.radiusSun", is(star2.getRadiusSun())))
                .andExpect(jsonPath("$.massSun", is(star2.getMassSun())));
    }

    @Test
    void updateByIdWithInvalidStar_shouldReturnBadRequestStatus() throws Exception {
        Long id = 1L;
        Star invalidStar = new Star(id, "New Star", 0, 728.7, 5.3, 5.8);
        mockMvc.perform(put("/api/stars/{id}", id, invalidStar)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidStar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById_inputValidId_shouldReturnNoContentStatus() throws Exception {
        doNothing().when(starService).deleteStar(anyLong());
        mockMvc.perform(delete("/api/stars/{id}", anyLong()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_inputNonExistentId_shouldReturnNotFoundStatus() throws Exception {
        long id = 1L;
        doThrow(new ResourceNotFoundException("With this ID: " + id + ", star does not exist."))
                .when(starService)
                .deleteStar(anyLong());
        mockMvc.perform(delete("/api/stars/{id}", anyLong()))
                .andExpect(status().isNotFound());
    }
}