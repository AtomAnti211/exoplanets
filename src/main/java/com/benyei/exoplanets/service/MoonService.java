package com.benyei.exoplanets.service;

import com.benyei.exoplanets.exception.ConstraintException;
import com.benyei.exoplanets.exception.NotUniqueException;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Moon;
import com.benyei.exoplanets.repository.MoonRepository;
import com.benyei.exoplanets.repository.PlanetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MoonService {

    private final MoonRepository moonRepository;

    private final PlanetRepository planetRepository;

    public MoonService(MoonRepository moonRepository, PlanetRepository planetRepository) {
        this.moonRepository = moonRepository;
        this.planetRepository = planetRepository;
    }

    public Moon saveMoon(Moon moon) {
        Optional<Moon> existingMoon = moonRepository.findMoonByName(moon.getName());
        if (existingMoon.isPresent()) {
            throw new NotUniqueException("Moon already exists with this name: " + moon.getName());
        }
        if (moon.getPlanet().getId() == null
                || planetRepository.findById(moon.getPlanet().getId()).isEmpty()) {
            throw new ConstraintException("Planet does not exist with this id, give me a real id");
        }
        return moonRepository.save(moon);
    }

    public List<Moon> getAllMoons() {
        return moonRepository.findAll();
    }

    public Moon getMoonById(long id) {
        return moonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Moon not found with id: " + id));
    }

    public Moon updateMoon(Moon moon, long id) {

        Optional<Moon> optionalMoon = moonRepository.findMoonByName(moon.getName());
        if (optionalMoon.isPresent() && optionalMoon.get().getId() != id) {
            throw new NotUniqueException(
                    "A moon with this name already exists under this name: " + moon.getName() +
                            " and not this identifier: " + id);
        }

        Moon existingMoon = moonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Moon not found with id: " + id));

        if (moon.getPlanet().getId() == null
                || planetRepository.findById(moon.getPlanet().getId()).isEmpty()) {
            throw new ConstraintException("Planet does not exist with this id, give me a real id");
        }

        existingMoon.setName(moon.getName());
        existingMoon.setPlanet(moon.getPlanet());
        existingMoon.setStatus(moon.getStatus());
        moonRepository.save(existingMoon);
        return existingMoon;
    }

    public void deleteMoon(long id) {
        moonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Moon not found with id: " + id));
        moonRepository.deleteById(id);
    }
}
