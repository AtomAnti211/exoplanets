package com.benyei.exoplanets.controller;

import com.benyei.exoplanets.exception.ConstraintException;
import com.benyei.exoplanets.exception.NotUniqueException;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Moon;
import com.benyei.exoplanets.service.MoonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/moons")
public class MoonController {

    @Autowired
    private MoonService moonService;

    @PostMapping()
    public ResponseEntity<?> saveMoon(@Valid @RequestBody Moon moon){
        try {
            return new ResponseEntity<>(moonService.saveMoon(moon), HttpStatus.CREATED);
        } catch (NotUniqueException | ConstraintException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMoons(){
        try {
            return new ResponseEntity<>(moonService.getAllMoons(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getMoonById(@PathVariable("id") long moonId){
        try {
            return new ResponseEntity<>(moonService.getMoonById(moonId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateMoon(@PathVariable("id") long id,
                                          @Valid @RequestBody Moon moon){
        try {
            return new ResponseEntity<>(moonService.updateMoon(moon, id), HttpStatus.OK);
        } catch (NotUniqueException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException | ConstraintException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteMoon(@PathVariable("id") long id){
        try {
            moonService.deleteMoon(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
