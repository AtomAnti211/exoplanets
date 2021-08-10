package com.benyei.exoplanets.controller;

import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Star;
import com.benyei.exoplanets.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/stars")
public class StarController {

    @Autowired
    private StarService starService;

    @PostMapping()
    public ResponseEntity<Star> saveStar(@Valid @RequestBody Star star){
        return new ResponseEntity<>(starService.saveStar(star), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Star> getAllStars(){
        return starService.getAllStars();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getStarById(@PathVariable("id") Long starId){
        try{
            return new ResponseEntity<>(starService.getStarById(starId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Star> updateStar(@PathVariable("id") Long id,
                                           @Valid @RequestBody Star star){
        return new ResponseEntity<>(starService.updateStar(star, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStar(@PathVariable("id") Long id) {
            starService.deleteStar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
