package com.benyei.exoplanets.controller;

import com.benyei.exoplanets.exception.ConstraintException;
import com.benyei.exoplanets.exception.NotUniqueException;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Planet;
import com.benyei.exoplanets.service.PlanetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    private final PlanetService planetService;

    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @PostMapping
    public ResponseEntity<?> savePlanet(@Valid @RequestBody Planet planet) {
        try {
            return new ResponseEntity<>(planetService.savePlanet(planet), HttpStatus.CREATED);
        } catch (NotUniqueException | ConstraintException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Planet> getAllPlanets() {
        return planetService.getAllPlanets();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPlanetById(@PathVariable("id") long planetId) {
        try {
            return new ResponseEntity<>(planetService.getPlanetById(planetId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updatePlanet(@PathVariable("id") long id,
                                          @Valid @RequestBody Planet planet) {
        try {
            return new ResponseEntity<>(planetService.updatePlanet(planet, id), HttpStatus.OK);
        } catch (NotUniqueException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException | ConstraintException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePlanet(@PathVariable("id") long id) {
        try {
            planetService.deletePlanet(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("habitablezone")
    public List<Planet> findAllByIsInTheHabitableZoneTrue() {
        return planetService.findAllByIsInTheHabitableZoneTrue();
    }
}
