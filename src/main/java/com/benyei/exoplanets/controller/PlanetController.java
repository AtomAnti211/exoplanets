package com.benyei.exoplanets.controller;

import com.benyei.exoplanets.model.Planet;
import com.benyei.exoplanets.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @PostMapping()
    public ResponseEntity<Planet> savePlanet(@Valid @RequestBody Planet planet){
        return new ResponseEntity<>(planetService.savePlanet(planet), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Planet> getAllPlanets(){
        return planetService.getAllPlanets();
    }

    @GetMapping("{id}")
    public ResponseEntity<Planet> getPlanetById(@PathVariable("id") long planetId){
        return new ResponseEntity<>(planetService.getPlanetById(planetId), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Planet> updatePlanet(@PathVariable("id") long id,
                                               @Valid @RequestBody Planet planet){
        return new ResponseEntity<>(planetService.updatePlanet(planet, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePlanet(@PathVariable("id") long id){
        planetService.deletePlanet(id);
        return new ResponseEntity<>("Planet deleted successfully!.", HttpStatus.NO_CONTENT);
    }
}
