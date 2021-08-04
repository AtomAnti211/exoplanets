package com.benyei.exoplanets.controller;

import com.benyei.exoplanets.model.Moon;
import com.benyei.exoplanets.service.MoonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moons")
public class MoonController {

    private final MoonService moonService;

    public MoonController(MoonService moonService) {
        this.moonService = moonService;
    }

    @PostMapping()
    public ResponseEntity<Moon> saveMoon(@RequestBody Moon moon){
        return new ResponseEntity<>(moonService.saveMoon(moon), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Moon> getAllMoons(){
        return moonService.getAllMoons();
    }

    @GetMapping("{id}")
    public ResponseEntity<Moon> getMoonById(@PathVariable("id") long moonId){
        return new ResponseEntity<>(moonService.getMoonById(moonId), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Moon> updateMoon(@PathVariable("id") long id
            ,@RequestBody Moon moon){
        return new ResponseEntity<>(moonService.updateMoon(moon, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMoon(@PathVariable("id") long id){

        moonService.deleteMoon(id);

        return new ResponseEntity<>("Moon deleted successfully!.", HttpStatus.OK);
    }
}
