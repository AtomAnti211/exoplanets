package com.benyei.exoplanets.service;

import com.benyei.exoplanets.exception.ConstraintException;
import com.benyei.exoplanets.exception.NotUniqueException;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Planet;
import com.benyei.exoplanets.repository.PlanetRepository;
import com.benyei.exoplanets.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private StarRepository starRepository;

    public Planet savePlanet(Planet planet) {
        Optional<Planet> existingPlanet = planetRepository.findPlanetByName(planet.getName());
        if (existingPlanet.isPresent()) {
            throw new NotUniqueException("Planet already exists with this name: " + planet.getName());
        }
        if (planet.getStar().getId() == null
                || starRepository.findById(planet.getStar().getId()).isEmpty()) {
            throw new ConstraintException("Star does not exist with this id, give me a real id");
        }
        return planetRepository.save(planet);
    }

    public List<Planet> getAllPlanets() {
        if (planetRepository.findAll().isEmpty()) {
            throw new ResourceNotFoundException("Not Found!");
        }
        return planetRepository.findAll();
    }

    public Planet getPlanetById(long id) {
        return planetRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Planet not found with id: " + id));

    }

    public Planet updatePlanet(Planet planet, long id) {

        Optional<Planet> optionalPlanet = planetRepository.findPlanetByName(planet.getName());
        if (optionalPlanet.isPresent() && optionalPlanet.get().getId() != id) {
            throw new NotUniqueException(
                    "A planet with this name already exists under this name: " + planet.getName() +
                            " and not this identifier: " + id);
        }

        Planet existingPlanet = planetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Planet not found with id: " + id));

        if (planet.getStar().getId() == null
                || starRepository.findById(planet.getStar().getId()).isEmpty()) {
            throw new ConstraintException("Star does not exist with this id, give me a real id");
        }

        existingPlanet.setName(planet.getName());
        existingPlanet.setYearOfDiscovery(planet.getYearOfDiscovery());
        existingPlanet.setDetection(planet.getDetection());
        existingPlanet.setStatusIsConfirmed(planet.getStatusIsConfirmed());
        existingPlanet.setStar(planet.getStar());
        planetRepository.save(existingPlanet);
        return existingPlanet;
    }

    public void deletePlanet(long id) {
        planetRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Planet not found with id: " + id));
        planetRepository.deleteById(id);
    }
}
