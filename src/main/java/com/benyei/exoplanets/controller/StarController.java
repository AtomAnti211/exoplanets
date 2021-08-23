package com.benyei.exoplanets.controller;

import com.benyei.exoplanets.exception.ConstraintException;
import com.benyei.exoplanets.exception.NotUniqueException;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Star;
import com.benyei.exoplanets.service.StarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stars")
public class StarController {

    private final StarService starService;

    public StarController(StarService starService) {
        this.starService = starService;
    }

    @PostMapping()
    public ResponseEntity<?> saveStar(@Valid @RequestBody Star star) {
        try {
            return new ResponseEntity<>(starService.saveStar(star), HttpStatus.CREATED);
        } catch (NotUniqueException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Star> getAllStars() {
        return starService.getAllStars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStarById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(starService.getStarById(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStar(@PathVariable("id") Long id,
                                        @Valid @RequestBody Star star) {
        try {
            return new ResponseEntity<>(starService.updateStar(star, id), HttpStatus.OK);
        } catch (NotUniqueException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStar(@PathVariable("id") Long id) {
        try {
            starService.deleteStar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ConstraintException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/distance")
    public List<Star> findAllByDistanceInLightYearsIsLessThan(@RequestParam Double distance) {
        return starService.findAllByDistanceInLightYearsIsLessThan(distance);
    }

    @GetMapping("/search")
    public List<Star> findAllByNameContaining(@RequestParam Optional<String> name) {
        if (name.isEmpty()) return starService.getAllStars();
        return starService.findAllByNameContaining(name);
    }
}
