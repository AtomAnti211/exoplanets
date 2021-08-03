package com.benyei.exoplanets.controller;

import com.benyei.exoplanets.model.Star;
import com.benyei.exoplanets.service.StarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/stars")
public class StarController {

    private final StarService starService;
    public StarController(StarService starService) {
        this.starService = starService;
    }

    @PostMapping()
    public ResponseEntity<Star> saveStar(@Valid @RequestBody Star star){
        return new ResponseEntity<>(starService.saveStar(star), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Star> getAllStars(){
        return starService.getAllStars();
    }

    @GetMapping("{id}")
    public ResponseEntity<Star> getStarById(@PathVariable("id") Long starId){
        return new ResponseEntity<>(starService.getStarById(starId), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Star> updateStar(@PathVariable("id") Long id,
                                           @Valid @RequestBody Star star){
        return new ResponseEntity<>(starService.updateStar(star, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteStar(@PathVariable("id") Long id){
        starService.deleteStar(id);
        return new ResponseEntity<>("Star deleted successfully!.", HttpStatus.OK);
    }
}
