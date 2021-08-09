package com.benyei.exoplanets.service.Impl;

import com.benyei.exoplanets.exception.NotUniqueException;
import com.benyei.exoplanets.exception.ResourceNotFoundException;
import com.benyei.exoplanets.model.Planet;
import com.benyei.exoplanets.repository.PlanetRepository;
import com.benyei.exoplanets.service.PlanetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanetServiceImpl implements PlanetService {


    private final PlanetRepository planetRepository;

    public PlanetServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Override
    public Planet savePlanet(Planet planet) {
        Optional<Planet> existingPlanet = planetRepository.findPlanetByName(planet.getName());
        if (existingPlanet.isPresent()) {
            throw  new NotUniqueException("Planet already exists with this name: " + planet.getName());
        }
        return planetRepository.save(planet);
    }

    @Override
    public List<Planet> getAllPlanets() {
        return planetRepository.findAll();
    }

    @Override
    public Planet getPlanetById(long id) {
        return planetRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Planet", "Id", id));

    }

    @Override
    public Planet updatePlanet(Planet planet, long id) {

        Optional<Planet> optionalPlanet = planetRepository.findPlanetByName(planet.getName());
        if (optionalPlanet.isPresent() && optionalPlanet.get().getId() != id) {
            throw  new NotUniqueException(
                    "A planet with this name already exists under this name: " + planet.getName() +
                            " and not this identifier: " + id);
        }

        Planet existingPlanet = planetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Planet", "Id", id));

        existingPlanet.setName(planet.getName());
        existingPlanet.setYearOfDiscovery(planet.getYearOfDiscovery());
        existingPlanet.setDetection(planet.getDetection());
        existingPlanet.setStatusIsConfirmed(planet.getStatusIsConfirmed());
        existingPlanet.setStar(planet.getStar());
        planetRepository.save(existingPlanet);
        return existingPlanet;
    }

    @Override
    public void deletePlanet(long id) {
        planetRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Planet", "Id", id));
        planetRepository.deleteById(id);
    }
}
